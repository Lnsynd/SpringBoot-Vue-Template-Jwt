package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.DetailsSaveVO;
import com.example.mapper.AccountDetailsMapper;
import com.example.service.AccountDetailsService;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Created by 刘千山 on 2023/11/16/016-21:44
 */
@Service
public class AccountDetailsServiceImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements AccountDetailsService {


    @Resource
    AccountService accountService;

    @Override
    public AccountDetails findAccountDetailsById(int id) {
        return this.getById(id);
    }

    @Override
    public synchronized boolean saveAccountDetails(int id, DetailsSaveVO detailsSaveVO) {
        Account account = accountService.findAccountByUsernameOrEmail(detailsSaveVO.getUsername());
        if (account == null || account.getId() == id) {
            accountService.update()
                    .eq("id", id)
                    .set("username", detailsSaveVO.getUsername())
                    .update();
            this.saveOrUpdate(new AccountDetails(
                    id,
                    detailsSaveVO.getGender(),
                    detailsSaveVO.getPhone(),
                    detailsSaveVO.getQq(),
                    detailsSaveVO.getWx(),
                    detailsSaveVO.getDesc()
            ));
            return true;
        }
        return false;
    }
}
