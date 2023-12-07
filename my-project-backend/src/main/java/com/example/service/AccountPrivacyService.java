package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVO;

/**
 * Created by 刘千山 on 2023/12/6/006-21:54
 */
public interface AccountPrivacyService extends IService<AccountPrivacy> {

    void savePrivacy(int id, PrivacySaveVO vo);
    AccountPrivacy accountPrivacy(int id);
}
