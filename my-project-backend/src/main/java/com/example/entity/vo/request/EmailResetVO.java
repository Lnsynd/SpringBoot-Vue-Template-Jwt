package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by 刘千山 on 2023/8/14/014-15:02
 */
@Data
public class EmailResetVO {

    @Email
    String email;
    @Length(min = 6, max = 6)
    String code;
    @Length(min = 6, max = 6)
    String password;
}
