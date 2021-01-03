package com.EGEA1R.CarService.web.controller;

import com.EGEA1R.CarService.service.interfaces.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderController {

    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService){
        this.orderService = orderService;
    }
}
