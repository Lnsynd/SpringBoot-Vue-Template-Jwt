package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by 刘千山 on 2023/12/12/012-23:39
 */
@Data
@TableName("db_image_store")
@AllArgsConstructor
public class StoreImage {
    Integer uid;
    String name;
    Date time;
}
