package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by 刘千山 on 2023/12/17/017-14:45
 */
@Data
public class TopicPreviewVO {
    Integer id;
    int type;
    String title;
    String text;
    List<String> images;
    Date time;
    Integer uid;
    String username;
    String avatar;

    int like;
    int collect;
}
