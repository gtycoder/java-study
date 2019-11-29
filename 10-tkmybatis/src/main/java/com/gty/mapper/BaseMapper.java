package com.gty.mapper;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BaseMapper<T> extends
        MySqlMapper<T>, Mapper<T>, ConditionMapper<T>, IdsMapper<T> {
    /*x
     * MySqlMapper是指mysql独有的一些通用的方法,   注意这个类不能被spring扫描
     * Mapper主要的方法来源,包含BaseMapper<T>,ExampleMapper<T>,RowBoundsMapper<T>,Marker {
     * ConditionMapper按照自定义的条件查询
     * IdsMapper按照ids查询,比较鸡肋,要求id是自增的.
     * */
}

