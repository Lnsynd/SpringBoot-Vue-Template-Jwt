package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.TopicTypeVO;
import com.example.entity.vo.response.WeatherVO;
import com.example.service.TopicService;
import com.example.service.WeatherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/weather")
    public RestBean<WeatherVO> weather(double longitude, double latitude) {
        WeatherVO vo = weatherService.fetchWeather(longitude, latitude);
        return vo == null ? RestBean.failure(400, "获取地理位置信息失败") : RestBean.success(vo);
    }

    @GetMapping("/types")
    public RestBean<List<TopicTypeVO>> listTypes() {
        return RestBean.success(topicService.listTypes().stream().map(topicType -> topicType.asViewObject(TopicTypeVO.class)).toList());
    }
}
