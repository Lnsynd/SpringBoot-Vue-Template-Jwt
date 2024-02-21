package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.TopicComment;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by 刘千山 on 2024/1/24/024-21:54
 */
@Mapper
public interface TopicCommentMapper extends BaseMapper<TopicComment> {
}
