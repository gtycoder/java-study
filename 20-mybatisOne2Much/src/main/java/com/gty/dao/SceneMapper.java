package com.gty.dao;

import com.gty.domain.Data;
import com.gty.domain.SceneDataMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SceneMapper {

    List<Data> selectDataList();
    List<SceneDataMapper> sceneDataMapperList();
    int sceneDataMapperCount();
}
