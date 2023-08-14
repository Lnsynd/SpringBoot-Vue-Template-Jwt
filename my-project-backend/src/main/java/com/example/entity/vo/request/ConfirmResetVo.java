package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 邮箱验证码封装类
 * Created by 刘千山 on 2023/8/14/014-15:00
 */
@Data
@AllArgsConstructor
public class ConfirmResetVo {
    @Email
    String email;
    @Length(min = 6, max = 6)
    String code;
}
