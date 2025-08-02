package com.example.credit_product.service;

import com.example.credit_product.model.DynamicRule;
import com.example.credit_product.repository.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

@Component
public class RuleEvaluator {
    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;

    private final Map<String, BiFunction<UUID, List<String>, Boolean>> queryHandlers = Map.of(
            "USER_OF", this::handleUserOf,
            "ACTIVE_USER_OF", this::handleActiveUserOf,
            "TRANSACTION_SUM_COMPARE", this::handleTransactionSumCompare,
            "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW", this::handleTransactionSumCompareDepositWithdraw
    );

    public RuleEvaluator(TransactionRepository transactionRepository, ObjectMapper objectMapper) {
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
    }

    public boolean evaluate(UUID userId, DynamicRule rule) {
        try {
            List<Map<String, Object>> queries = objectMapper.readValue(
                    rule.getRuleJson(),
                    new TypeReference<>() {
                    });

            return queries.stream().allMatch(query -> {
                String queryType = (String) query.get("query");
                List<String> arguments = (List<String>) query.get("arguments");
                boolean negate = (boolean) query.get("negate");

                boolean result = queryHandlers.get(queryType).apply(userId, arguments);
                return negate ? !result : result;
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при оценке правила", e);
        }
    }

    private boolean handleUserOf(UUID userId, List<String> arguments) {
        String productType = arguments.get(0);
        return transactionRepository.hasTransactionsOfProductType(userId, productType);
    }

    private boolean handleActiveUserOf(UUID userId, List<String> arguments) {
        String productType = arguments.get(0);
        return transactionRepository.countTransactionsByUserAndProductType(userId, productType) >= 5;
    }

    private boolean handleTransactionSumCompare(UUID userId, List<String> arguments) {
        String productType = arguments.get(0);
        String transactionType = arguments.get(1);
        String operator = arguments.get(2);
        long value = Long.parseLong(arguments.get(3));

        long sum = transactionType.equals("DEPOSIT")
                ? transactionRepository.getTotalDepositsByProductType(userId, productType)
                : transactionRepository.getTotalExpensesByProductType(userId, productType);

        return compare(sum, operator, value);
    }

    private boolean handleTransactionSumCompareDepositWithdraw(UUID userId, List<String> arguments) {
        String productType = arguments.get(0);
        String operator = arguments.get(1);

        long deposits = transactionRepository.getTotalDepositsByProductType(userId, productType);
        long withdrawals = transactionRepository.getTotalExpensesByProductType(userId, productType);

        return compare(deposits, operator, withdrawals);
    }

    private boolean compare(long a, String operator, long b) {
        return switch (operator) {
            case ">" -> a > b;
            case "<" -> a < b;
            case "=" -> a == b;
            case ">=" -> a >= b;
            case "<=" -> a <= b;
            default -> throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        };
    }
}