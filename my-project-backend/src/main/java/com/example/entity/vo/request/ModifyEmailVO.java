package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by 刘千山 on 2023/11/25/025-15:15
 */
@Data
public class ModifyEmailVO {

    @Email
    String email;

    @Length(min = 6, max = 6)
    String code;
}
