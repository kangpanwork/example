package com.sanri.test.sharding.controller;

import com.sanri.test.sharding.mapper.OrderMapper;
import com.sanri.test.sharding.mapper.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sharding")
public class ShardingController {
    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/insert")
    public void insert(){
        RandomDataService randomDataService = new RandomDataService();
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order order = (Order) randomDataService.populateData(Order.class);
            orders.add(order);
        }
        orderMapper.insert(orders);
    }
}
