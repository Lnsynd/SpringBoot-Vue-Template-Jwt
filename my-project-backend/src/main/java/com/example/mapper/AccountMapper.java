package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by 刘千山 on 2023/8/10/010-15:21
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
