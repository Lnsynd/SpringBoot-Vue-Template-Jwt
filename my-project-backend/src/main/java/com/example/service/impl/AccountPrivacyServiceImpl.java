package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVO;
import com.example.mapper.AccountPrivacyMapper;
import com.example.service.AccountPrivacyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by 刘千山 on 2023/12/6/006-21:54
 */
@Service
public class AccountPrivacyServiceImpl extends ServiceImpl<AccountPrivacyMapper, AccountPrivacy> implements AccountPrivacyService {
    @Override
    @Transactional
    public void savePrivacy(int id, PrivacySaveVO vo) {
        AccountPrivacy privacy = Optional.ofNullable(this.getById(id)).orElse(new AccountPrivacy(id));
        boolean status = vo.isStatus();
        switch (vo.getType()){
            case "phone" -> privacy.setPhone(status);
            case "wx" -> privacy.setWx(status);
            case "email" -> privacy.setEmail(status);
            case "qq" -> privacy.setQq(status);
            case "gender" -> privacy.setGender(status);
        }
        this.saveOrUpdate(privacy);
    }


    public AccountPrivacy accountPrivacy(int id){
        return Optional.ofNullable(this.getById(id)).orElse(new AccountPrivacy(id));
    }
}