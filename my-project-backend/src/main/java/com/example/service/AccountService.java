package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by 刘千山 on 2023/8/10/010-15:21
 */
public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByUsernameOrEmail(String text);
}
