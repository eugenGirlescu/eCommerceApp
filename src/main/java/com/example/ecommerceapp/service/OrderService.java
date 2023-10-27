package com.example.ecommerceapp.service;

import com.example.ecommerceapp.dto.UserGetDTO;
import com.example.ecommerceapp.model.Order;
import com.example.ecommerceapp.model.User;
import com.example.ecommerceapp.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public List<Order> getOrders(User user) {
        return orderRepository.findByUser(user);
    }
}
