package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by 刘千山 on 2023/8/11/011-17:16
 */
@Data
public class EmailRegisterVO {

    @Email
    @Length(min = 7)
    String email;

    @Length(max = 6, min = 6)
    String code;

    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    @Length(min = 1, max = 10)
    String username;

    @Length(min = 6, max = 15)
    String password;
}
