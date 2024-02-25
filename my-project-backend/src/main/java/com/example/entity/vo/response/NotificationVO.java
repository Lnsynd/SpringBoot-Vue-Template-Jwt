package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by 刘千山 on 2024/2/25/025-21:32
 */
@Data
public class NotificationVO {
    Integer id;
    String title;
    String content;
    String type;
    String url;
    Date time;
}
