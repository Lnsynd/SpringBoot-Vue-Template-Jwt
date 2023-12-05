package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by 刘千山 on 2023/11/6/006-21:44
 */
@Data
public class AccountVO {
    String username;
    String email;
    String role;
    Date registerTime;
}
