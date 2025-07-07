package com.example.credit_product.model;

import java.util.UUID;

public record User(
        UUID id,
        String name
) {}