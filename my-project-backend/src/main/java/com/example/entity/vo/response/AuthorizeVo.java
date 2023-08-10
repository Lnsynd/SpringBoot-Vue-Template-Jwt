package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * 登录验证信息 返回 封装类
 * Created by 刘千山 on 2023/8/8/008-21:19
 */
@Data
public class AuthorizeVo {
    String username;
    String role;
    String token;
    Date expire;
}
