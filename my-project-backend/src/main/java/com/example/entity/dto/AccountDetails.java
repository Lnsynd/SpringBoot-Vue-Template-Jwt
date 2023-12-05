package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 刘千山 on 2023/11/16/016-21:35
 */

@Data
@TableName("db_account_details")
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails implements BaseData {

    @TableId
    Integer id;
    int gender;
    String phone;
    String qq;
    String wx;
    @TableField("`desc`")
    String desc;

}
