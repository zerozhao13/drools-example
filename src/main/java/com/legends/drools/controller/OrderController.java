package com.legends.drools.controller;

import com.legends.drools.entity.ClothOrder;
import com.legends.drools.service.OrderServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

        @Resource OrderServiceImpl orderDetailService;
        @PutMapping("")
        public void addOrder(@RequestBody ClothOrder order) {
                orderDetailService.payOrder(order);
        }
}
