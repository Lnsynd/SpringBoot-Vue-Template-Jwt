package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by 刘千山 on 2023/12/27/027-21:34
 */
@Data
public class TopicDetailVO {

    Integer id;
    String title;
    String content;
    Integer type;
    Date time;
    User user;
    Interact interact;
    Long comments;


    @Data
    @AllArgsConstructor
    public static class Interact{
        Boolean like;
        Boolean collect;
    }

    @Data
    public static class User{
        Integer id;
        String username;
        String avatar;
        String desc;
        Integer gender;
        String qq;
        String wx;
        String phone;
        String email;
    }
}
