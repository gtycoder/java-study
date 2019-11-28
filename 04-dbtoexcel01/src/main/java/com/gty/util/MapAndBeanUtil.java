package com.gty.util;

import org.springframework.cglib.beans.BeanMap;

import java.util.*;

public class MapAndBeanUtil {
    /**
     * 将实体类转换为一个Map
     */
    public static <T> Map<String,Object> beanToMap (T bean) {
        Map<String, Object> map = new HashMap<>();
        //创建一个BeanMap
        BeanMap beanMap = BeanMap.create(bean);
        //将BeanMap转换为Map
        Iterator iterator = beanMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            map.put(key, beanMap.get(key));
        }
        return map;
    }

    /**
     * 实体类集合转换为List<Map>
     */
    public static <T> List<Map<String, Object>> beansToMap(List<T> beanList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        Iterator<T> iterator = beanList.iterator();
        while (iterator.hasNext()) {
            T bean = iterator.next();
            Map<String, Object> map = beanToMap(bean);
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 将map转化为实体类
     */
    public static <T> T mapToBean(Map<String, Object> map,Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T bean = clazz.newInstance();
        BeanMap beanMap = BeanMap.create(bean);
        //将map转换为bean
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将List<Map>转化为实体类
     */
    public static <T> List<T> mapsToBeans(List<Map<String, Object>> mapList, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        List<T> list = new ArrayList<>();
        Iterator<Map<String, Object>> iterator = mapList.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            T bean = mapToBean(map, clazz);
            list.add(bean);
        }
        return list;
    }
}
