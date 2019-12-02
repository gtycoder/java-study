package com.gty.dao;

import com.gty.domain.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    int findOrderCount();

    int addOrder(Order order);
}
