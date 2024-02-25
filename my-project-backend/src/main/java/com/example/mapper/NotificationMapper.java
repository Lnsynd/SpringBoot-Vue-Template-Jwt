package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by 刘千山 on 2024/2/25/025-21:28
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
