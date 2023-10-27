package com.example.ecommerceapp.repository;

import com.example.ecommerceapp.model.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Long> {
    Optional<User> findByUserNameIgnoreCase(String username);
    Optional<User> findByEmailIgnoreCase(String email);
}
