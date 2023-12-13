package com.example.service.impl;

import com.example.entity.dto.TopicType;
import com.example.mapper.TopicTypeMapper;
import com.example.service.TopicService;
import jakarta.annotation.Resource;
import net.sf.jsqlparser.statement.select.Top;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 刘千山 on 2023/12/13/013-22:06
 */
@Service
public class TopicServiceImpl implements TopicService {
    @Resource
    TopicTypeMapper topicTypeMapper;
    @Override
    public List<TopicType> listTypes() {
        return topicTypeMapper.selectList(null);
    }
}
