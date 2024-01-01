package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;
import org.bouncycastle.util.Strings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 刘千山 on 2023/12/6/006-21:51
 */
@Data
@TableName("db_account_privacy")
public class AccountPrivacy implements BaseData {

    @TableId(type = IdType.AUTO)
    final Integer id;

    boolean phone = true;
    boolean wx = true;
    boolean qq = true;
    boolean email = true;
    boolean gender = true;

    public String[] hiddenFields(){
        List<String> strings = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if(field.getType().equals(boolean.class) && !field.getBoolean(this)){
                    strings.add(field.getName());
                }
            }catch (IllegalAccessException e){
                throw new RuntimeException(e);
            }
        }
        return strings.toArray(String[]::new);
    }
}
