package com.example.ecommerceapp.repository;

import com.example.ecommerceapp.dto.UserGetDTO;
import com.example.ecommerceapp.model.Order;
import com.example.ecommerceapp.model.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface OrderRepository extends ListCrudRepository<Order, Long> {
    List<Order> findByUser(User user);
}
