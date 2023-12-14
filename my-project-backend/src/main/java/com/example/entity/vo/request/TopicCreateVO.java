package com.example.entity.vo.request;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by 刘千山 on 2023/12/14/014-22:30
 */
@Data
public class TopicCreateVO {

    int type;
    @Length(min = 1, max = 30)
    String title;
    JSONObject content;
}
