package com.example.entity.vo.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * Created by 刘千山 on 2024/1/24/024-21:40
 */
@Data
public class AddCommentVO {
    @Min(1)
    int tid;
    String content;
    @Min(-1)
    int quote;

}
