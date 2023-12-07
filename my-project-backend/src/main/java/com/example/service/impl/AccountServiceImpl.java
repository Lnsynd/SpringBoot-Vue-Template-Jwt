package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.*;
import com.example.mapper.AccountMapper;
import com.example.service.AccountService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by 刘千山 on 2023/8/10/010-15:22
 */
@Slf4j
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    FlowUtils flowUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.findAccountByUsernameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account findAccountByUsernameOrEmail(String text) {
        return this.query()
                .eq("username", text)
                .or()
                .eq("email", text)
                .one();
    }

    /**
     * @return 通过id返回用户
     */
    @Override
    public Account findAccountById(int id) {
        return this.query().eq("id", id).one();
    }

    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()) {
            if (!this.verifyLimit(ip))
                return "请求频繁，请稍后再试";
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            sendMessage(type, email, code);
            return null;
        }
    }

    @Async("asyncServiceExecutor")
    protected void sendMessage(String type, String email, int code) {
        long before = System.currentTimeMillis();
        Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
        amqpTemplate.convertAndSend("mail", data);
        stringRedisTemplate.opsForValue().set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
        long now = System.currentTimeMillis();
        log.info("执行该方法用时为:" + (now - before));
    }


    private boolean verifyLimit(String ip) {
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }

    @Resource
    PasswordEncoder encoder;

    @Override
    public String registerEmailAccount(EmailRegisterVO emailRegisterVo) {
        String email = emailRegisterVo.getEmail();
        String username = emailRegisterVo.getUsername();
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + email);
        String key = Const.VERIFY_EMAIL_DATA + email;
        if (code == null) return "请获取验证码";
        if (!code.equals(emailRegisterVo.getCode())) return "验证码输入错误，请重新输入";
        if (this.isExistAccountByEmail(email)) return "此电子邮件已被其他用户注册";
        if (this.isExistAccountByUsername(username)) return "此用户名已被其他用户注册";
        String password = encoder.encode(emailRegisterVo.getPassword());
        Account account = new Account(null, username, password, email, "user", null, new Date());
        if (this.save(account)) {
            stringRedisTemplate.delete(key);
            return null;
        } else {
            return "内部错误，请联系管理员";
        }

    }

    private boolean isExistAccountByEmail(String email) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email", email));
    }

    private boolean isExistAccountByUsername(String username) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username", username));
    }

    @Override
    public String resetPasswordConfirm(ConfirmResetVO vo) {
        String email = vo.getEmail();
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + email);
        if (code == null) {
            return "请先获取验证码";
        }
        if (!code.equals(vo.getCode())) {
            return "验证码错误，请重新输入";
        }
        return null;
    }

    @Override
    public String resetPassword(EmailResetVO vo) {
        String email = vo.getEmail();
        String verify = this.resetPasswordConfirm(new ConfirmResetVO(vo.getEmail(), vo.getCode()));
        if (verify != null) return verify;
        String password = encoder.encode(vo.getPassword());
        boolean update = this.update().eq("email", email).set("password", password).update();
        if (update) {
            stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA + email);
        }
        return null;
    }

    /**
     * 修改绑定邮箱地址
     */
    @Override
    public String modifyEmail(int id, ModifyEmailVO vo) {
        String email = vo.getEmail();
        String code = getEmailVerifyCode(email);
        if (code == null) {
            return "请先获取验证码";
        }
        if (!code.equals(vo.getCode())) {
            return "验证码错误";
        }
        this.deleteEmailVerifyCode(email);
        Account account = this.findAccountByUsernameOrEmail(email);
        if (account != null) {
            return "该地址已被使用";
        }
        this.update()
                .set("email", email).eq("id", id).update();
        return null;
    }

    /**
     * 删除当前邮箱的验证码
     *
     * @param email 邮箱地址
     */
    private void deleteEmailVerifyCode(String email) {
        String key = Const.VERIFY_EMAIL_DATA + email;
        stringRedisTemplate.delete(key);
    }

    /**
     * 获取Redis中验证码
     *
     * @param email 邮箱地址
     */
    private String getEmailVerifyCode(String email) {
        String key = Const.VERIFY_EMAIL_DATA + email;
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public String changePassword(int id, ChangePasswordVO vo) {
        String password = this.query().eq("id", id).one().getPassword();
        if (!passwordEncoder.matches(vo.getPassword(), password))
            return "密码错误,请重新输入";
        boolean success = this.update()
                .eq("id", id)
                .set("password", passwordEncoder.encode(vo.getRepeat_new_password())).update();
        return success ? null : "未知错误，请联系管理员";
    }
}
