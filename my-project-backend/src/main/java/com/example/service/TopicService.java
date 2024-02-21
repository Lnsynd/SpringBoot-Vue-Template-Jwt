package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Interact;
import com.example.entity.dto.Topic;
import com.example.entity.dto.TopicType;
import com.example.entity.vo.request.AddCommentVO;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.request.TopicUpdateVO;
import com.example.entity.vo.response.CommentVO;
import com.example.entity.vo.response.TopicDetailVO;
import com.example.entity.vo.response.TopicPreviewVO;
import com.example.entity.vo.response.TopicTopVO;

import java.util.List;

/**
 * Created by 刘千山 on 2023/12/13/013-22:06
 */
public interface TopicService extends IService<Topic> {
    List<TopicType> listTypes();

    String createTopic(int uid, TopicCreateVO vo);
    List<TopicPreviewVO> listTopicByPage(int page,int type);
    List<TopicTopVO> listTopTopics();

    TopicDetailVO getTopic(int tid,int uid);

    void interact(Interact interact,boolean state);

    List<TopicPreviewVO> listTopicsCollects(int uid);

    String updateTopic(int uid, TopicUpdateVO vo);

    String createComment(AddCommentVO vo,int uid);
    List<CommentVO> comments(int tid,int pageNumber);
}
