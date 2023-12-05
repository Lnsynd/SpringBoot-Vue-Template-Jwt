package com.example.entity.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by 刘千山 on 2023/12/5/005-22:14
 */
@Data
public class ChangePasswordVO {
    @Length(min = 6,max = 20)
    String password;
    @Length(min = 6,max = 20)
    String new_password;
    @Length(min = 6,max = 20)
    String repeat_new_password;
}
