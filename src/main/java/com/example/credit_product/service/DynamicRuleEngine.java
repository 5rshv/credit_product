package com.example.credit_product.service;

import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.dto.RuleQueryDTO;
import com.example.credit_product.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DynamicRuleEngine {

    private final TransactionRepository transactionRepository;

    public DynamicRuleEngine(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean evaluateRule(DynamicRuleDTO rule, UUID userId) {
        for (RuleQueryDTO query : rule.getRule()) {
            boolean queryResult = executeQuery(query, userId);

            // Если любой запрос вернул false, правило не выполняется
            if (!queryResult) {
                return false;
            }
        }
        return true;
    }

    private boolean executeQuery(RuleQueryDTO query, UUID userId) {
        switch (query.getQuery()) {
            case "USER_OF":
                return transactionRepository.hasTransactionsOfProductType(userId, query.getArguments().get(0));

            case "ACTIVE_USER_OF":
                return transactionRepository.countTransactionsByUserAndProductType(userId, query.getArguments().get(0)) >= 5;

            case "TRANSACTION_SUM_COMPARE":
                String productType = query.getArguments().get(0);
                String transactionType = query.getArguments().get(1);
                String operator = query.getArguments().get(2);
                long threshold = Long.parseLong(query.getArguments().get(3));

                long sum = getTransactionSum(userId, productType, transactionType);
                return compareValues(sum, threshold, operator);

            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                String prodType = query.getArguments().get(0);
                String op = query.getArguments().get(1);

                long deposits = transactionRepository.getTotalDepositsByProductType(userId, prodType);
                long withdrawals = transactionRepository.getTotalExpensesByProductType(userId, prodType);

                return compareValues(deposits, withdrawals, op);

            default:
                throw new IllegalArgumentException("Неизвестный тип запроса: " + query.getQuery());
        }
    }

    private long getTransactionSum(UUID userId, String productType, String transactionType) {
        if ("DEPOSIT".equals(transactionType)) {
            return transactionRepository.getTotalDepositsByProductType(userId, productType);
        } else if ("WITHDRAW".equals(transactionType)) {
            return transactionRepository.getTotalExpensesByProductType(userId, productType);
        }
        throw new IllegalArgumentException("Неизвестный тип транзакции: " + transactionType);
    }

    private boolean compareValues(long left, long right, String operator) {
        switch (operator) {
            case ">": return left > right;
            case "<": return left < right;
            case "=": return left == right;
            case ">=": return left >= right;
            case "<=": return left <= right;
            default: throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        }
    }
}