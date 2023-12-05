package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.AccountDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by 刘千山 on 2023/11/16/016-21:39
 */
@Mapper
public interface AccountDetailsMapper extends BaseMapper<AccountDetails> {

}
