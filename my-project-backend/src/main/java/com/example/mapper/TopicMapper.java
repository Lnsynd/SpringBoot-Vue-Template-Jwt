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
    @Select("""
            select * from db_topic left join db_account on db_topic.uid = db_account.id
                    order by `time` desc limit #{start}, 10 
            """
    )
    List<Topic> topicList(int start);

    @Select("""
            select * from db_topic left join db_account on db_topic.uid = db_account.id
            where type = #{type}
            order by `time` desc limit #{start}, 10 
            """
    )
    List<Topic> topicListByType(int start,int type);
}
