package com.example.service;

import com.example.entity.vo.response.WeatherVO;

/**
 * Created by 刘千山 on 2023/12/10/010-16:28
 */
public interface WeatherService {
    WeatherVO fetchWeather(double longitude,double latitude);
}
