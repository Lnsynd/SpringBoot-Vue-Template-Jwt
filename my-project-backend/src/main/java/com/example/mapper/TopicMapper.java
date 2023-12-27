package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 刘千山 on 2023/12/14/014-22:27
 */
@Mapper
public interface TopicMapper extends BaseMapper<Topic> {

}
