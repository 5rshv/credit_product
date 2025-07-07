package com.example.credit_product.model;

import java.time.LocalDate;
import java.util.UUID;

public record Transaction(
        UUID id,
        UUID userId,
        Product product,
        TransactionType type,
        long amount,
        LocalDate date
) {}