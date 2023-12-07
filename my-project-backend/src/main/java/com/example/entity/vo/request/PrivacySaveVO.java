package com.example.entity.vo.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Created by 刘千山 on 2023/12/6/006-21:46
 */

@Data
public class PrivacySaveVO {

    @Pattern(regexp = "(phone|email|qq|wx|gender)")
    String type;
    boolean status;

}
