package com.example.ecommerceapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tbl_order_quantity")
public class OrderQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, name = "quantity")
    private Integer quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
