package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.response.*;
import com.example.service.TopicService;
import com.example.service.WeatherService;
import com.example.utils.Const;
import com.example.utils.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 刘千山 on 2023/12/10/010-16:23
 */
@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Resource
    WeatherService weatherService;

    @Resource
    TopicService topicService;

    @Resource
    ControllerUtils controllerUtils;

    @GetMapping("/weather")
    public RestBean<WeatherVO> weather(double longitude, double latitude) {
        WeatherVO vo = weatherService.fetchWeather(longitude, latitude);
        return vo == null ? RestBean.failure(400, "获取地理位置信息失败") : RestBean.success(vo);
    }

    @GetMapping("/types")
    public RestBean<List<TopicTypeVO>> listTypes() {
        return RestBean.success(topicService.listTypes().stream().map(topicType -> topicType.asViewObject(TopicTypeVO.class)).toList());
    }


    @PostMapping("/create-topic")
    public RestBean<Void> createTopic(@Valid @RequestBody TopicCreateVO vo,
                                      @RequestAttribute(Const.ATTR_USER_ID) int id) {
        return controllerUtils.messageHandle(() -> topicService.createTopic(id, vo));
    }

    @GetMapping("/list-topic")
    public RestBean<List<TopicPreviewVO>> listTopic(@RequestParam @Min(0) int page,
                                                    @RequestParam @Min(0) int type) {
        return RestBean.success(topicService.listTopicByPage(page + 1, type));
    }

    @GetMapping("/top-topic")
    public RestBean<List<TopicTopVO>> topTopic() {
        return RestBean.success(topicService.listTopTopics());
    }


    @GetMapping("/topic")
    public RestBean<TopicDetailVO> topic(@RequestParam @Min(0) int tid){
        return RestBean.success(topicService.getTopic(tid));
    }
}
