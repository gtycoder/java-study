package com.gty.controller;

import com.gty.dao.SceneMapper;
import com.gty.domain.Scene;
import com.gty.domain.SceneDataMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {
    @Resource
    private SceneMapper sceneMapper;

/*    @RequestMapping("/page")
    public String page() {

        List<Scene> sceneList =  sceneMapper.selectAll();
        for (Scene scene : sceneList) {
            System.out.println(scene);
        }
        return "555";
    }*/

    @RequestMapping("/page22")
    public String page22() {

        List<SceneDataMapper> sceneList =  sceneMapper.sceneDataMapperList();
        for (SceneDataMapper scene : sceneList) {
            System.out.println(scene);
        }
        return "555";
    }

    @RequestMapping("/page33")
    public String page33() {
        int sum = sceneMapper.sceneDataMapperCount();
        System.out.println(sum);
        List<SceneDataMapper> sceneList =  sceneMapper.sceneDataMapperList();
        for (SceneDataMapper scene : sceneList) {
            System.out.println(scene);
        }
        return "555";
    }

}
