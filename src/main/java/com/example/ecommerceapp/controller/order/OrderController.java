package com.example.ecommerceapp.controller.order;

import com.example.ecommerceapp.dto.UserGetDTO;
import com.example.ecommerceapp.model.Order;
import com.example.ecommerceapp.model.User;
import com.example.ecommerceapp.service.OrderService;
import com.example.ecommerceapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public List<Order> getOrders(@AuthenticationPrincipal User user) {
        return orderService.getOrders(user);
    }
}
