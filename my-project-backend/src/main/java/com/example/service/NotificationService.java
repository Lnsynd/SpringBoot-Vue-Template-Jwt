package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Notification;
import com.example.entity.vo.response.NotificationVO;

import java.util.List;

/**
 * Created by 刘千山 on 2024/2/25/025-21:29
 */
public interface NotificationService extends IService<Notification> {
    List<NotificationVO> findUserNotification(int uid);
    void deleteUserNotification(int id,int uid);
    void deleteUserAllNotification(int uid);
    void addNotification(int uid,String title,String content,String type,String url);
}
