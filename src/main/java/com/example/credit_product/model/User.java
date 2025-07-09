package com.example.credit_product.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "users")
public record User(
       @Id UUID id,
        String name
) {}