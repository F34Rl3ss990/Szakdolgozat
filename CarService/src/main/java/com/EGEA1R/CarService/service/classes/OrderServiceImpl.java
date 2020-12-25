package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.repository.OrderRepository;
import com.EGEA1R.CarService.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }
}
