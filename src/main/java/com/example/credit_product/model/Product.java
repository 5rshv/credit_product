package com.example.credit_product.model;

import java.util.UUID;

public record Product(
        UUID id,
        String name,
        ProductType type
) {}