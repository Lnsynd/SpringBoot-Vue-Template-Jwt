package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.*;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by 刘千山 on 2023/8/10/010-15:21
 */
public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByUsernameOrEmail(String text);
    Account findAccountById(int id);

    /**
     * 发送邮箱验证码
     */
    String registerEmailVerifyCode(String type,String email,String ip);

    /**
     * 邮箱注册账号
     */
    String registerEmailAccount(EmailRegisterVO emailRegisterVo);

    /**
     * 邮箱重置密码验证
     */
    String resetPasswordConfirm(ConfirmResetVO vo);

    /**
     * 重置密码
     */
    String resetPassword(EmailResetVO vo);


    /**
     * 修改电子邮件地址
     */
    String modifyEmail(int id, ModifyEmailVO vo);

    /**
     * 修改密码
     */
    String changePassword(int id, ChangePasswordVO vo);
}
