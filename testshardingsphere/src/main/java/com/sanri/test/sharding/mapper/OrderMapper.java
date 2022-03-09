package com.sanri.test.sharding.mapper;

import com.sanri.test.sharding.mapper.entity.Order;

import java.util.List;

public interface OrderMapper {

    void insert(List<Order> list);
}
