package com.example.credit_product.model;

import java.util.UUID;

public record ProductType(
        UUID id,
        String code,  // например, "SAVING"
        String name   // например, "Сберегательный счёт"
) {}