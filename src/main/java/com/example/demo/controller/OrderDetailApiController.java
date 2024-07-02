package com.example.demo.controller;

import com.example.demo.model.OrderDetail;
import com.example.demo.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailApiController {

    private final OrderService _orderService;

    public OrderDetailApiController(OrderService orderService) {
        _orderService = orderService;
    }

    @GetMapping
    public List<OrderDetail> getAllOrderDetails() {
        return _orderService.getAllOrderDetails();
    }
}

