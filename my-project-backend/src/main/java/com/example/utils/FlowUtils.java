package com.example.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 限流工具
 * Created by 刘千山 on 2023/8/11/011-16:07
 */
@Component
public class FlowUtils {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    public boolean limitOnceCheck(String IpKey, int blockTime) {
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(IpKey))) {
            return false;
        }else{
            stringRedisTemplate.opsForValue().set(IpKey,"",blockTime, TimeUnit.SECONDS);
            return true;
        }
    }
}
