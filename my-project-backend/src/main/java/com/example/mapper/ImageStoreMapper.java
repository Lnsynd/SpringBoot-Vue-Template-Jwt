package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.StoreImage;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by 刘千山 on 2023/12/12/012-23:40
 */
@Mapper
public interface ImageStoreMapper extends BaseMapper<StoreImage> {
}
