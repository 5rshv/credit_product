package com.example.credit_product.model;

import java.util.UUID;

public record TransactionType(
        UUID id,
        String code,  // например, "DEPOSIT"
        String name   // например, "Пополнение"
) {}