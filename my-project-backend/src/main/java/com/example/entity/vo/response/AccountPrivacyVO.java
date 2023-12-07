package com.example.entity.vo.response;

import lombok.Data;

/**
 * Created by 刘千山 on 2023/12/6/006-22:05
 */

@Data
public class AccountPrivacyVO {
    boolean phone;
    boolean wx;
    boolean qq;
    boolean email;
    boolean gender;
}
