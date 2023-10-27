package com.example.ecommerceapp.repository;

import com.example.ecommerceapp.model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductRepository extends ListCrudRepository<Product, Long> {
}
