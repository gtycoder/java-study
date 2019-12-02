package com.gty.dao;

import com.gty.domain.Store;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper {

    Store findProductNumById(String id);

    int updateProductNumById(Store store);

    int updateProductNumById11111(Store store);
}
