package com.example.credit_product.service;

import com.example.credit_product.repository.TransactionRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CachedTransactionService {

    private final TransactionRepository transactionRepository;

    private final Cache<String, Boolean> userOfCache = Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    private final Cache<String, Long> sumCache = Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    public CachedTransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean hasTransactionsOfProductType(UUID userId, String productType) {
        String key = userId + ":" + productType;
        return userOfCache.get(key, k -> transactionRepository.hasTransactionsOfProductType(userId, productType));
    }

    public long getTotalDepositsByProductType(UUID userId, String productType) {
        String key = userId + ":" + productType + ":DEPOSIT";
        return sumCache.get(key, k -> transactionRepository.getTotalDepositsByProductType(userId, productType));
    }

    public long getTotalExpensesByProductType(UUID userId, String productType) {
        String key = userId + ":" + productType + ":WITHDRAW";
        return sumCache.get(key, k -> transactionRepository.getTotalExpensesByProductType(userId, productType));
    }
}