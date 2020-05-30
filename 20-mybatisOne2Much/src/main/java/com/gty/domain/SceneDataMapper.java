package com.gty.domain;

import lombok.Data;

import java.util.List;

@Data
public class SceneDataMapper {
    private int id;
    private String sceneCode;
    private List<com.gty.domain.Data> dataList;
}
