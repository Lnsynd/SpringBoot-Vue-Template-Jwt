package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created by 刘千山 on 2024/1/24/024-21:53
 */
@Data
@TableName("db_topic_comment")
public class TopicComment {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer uid;
    Integer tid;
    String content;
    Date time;
    Integer quote;
}
