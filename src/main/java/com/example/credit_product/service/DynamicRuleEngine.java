package com.example.credit_product.service;

import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.dto.RuleQueryDTO;
import com.example.credit_product.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DynamicRuleEngine {

    private final TransactionRepository transactionRepository;

    public DynamicRuleEngine(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean evaluateRule(DynamicRuleDTO rule, UUID userId) {
        if (rule == null || rule.getRule() == null) {
            throw new IllegalArgumentException("DynamicRuleDTO или его список правил равен null");
        }

        // ОЧИСТКА: убираем условия с пустым query или null вообще
        List<RuleQueryDTO> filteredQueries = rule.getRule().stream()
                .filter(q -> q != null && q.getQuery() != null && !q.getQuery().isBlank())
                .collect(Collectors.toList());

        if (filteredQueries.isEmpty()) {
            // Нет валидных подусловий, правило невозможно проверить — считаем невыполненным
            return false;
        }

        for (RuleQueryDTO query : filteredQueries) {
            boolean queryResult = executeQuery(query, userId);

            // Если любой запрос вернул false, правило не выполняется
            if (!queryResult) {
                return false;
            }
        }
        return true;
    }

    private boolean executeQuery(RuleQueryDTO query, UUID userId) {
        List<String> args = query.getArguments();
        String queryType = query.getQuery();
        switch (queryType) {
            case "USER_OF":
                if (args == null || args.size() < 1 || args.get(0) == null) {
                    throw new IllegalArgumentException("Missing argument for USER_OF query: " + query);
                }
                return transactionRepository.hasTransactionsOfProductType(userId, args.get(0));

            case "ACTIVE_USER_OF":
                if (args == null || args.size() < 1 || args.get(0) == null) {
                    throw new IllegalArgumentException("Missing argument for ACTIVE_USER_OF query: " + query);
                }
                return transactionRepository.countTransactionsByUserAndProductType(userId, args.get(0)) >= 5;

            case "TRANSACTION_SUM_COMPARE":
                if (args == null || args.size() < 4
                        || args.get(0) == null || args.get(1) == null
                        || args.get(2) == null || args.get(3) == null) {
                    throw new IllegalArgumentException("Missing arguments for TRANSACTION_SUM_COMPARE: " + query);
                }
                String productType = args.get(0);
                String transactionType = args.get(1);
                String operator = args.get(2);
                long threshold;
                try {
                    threshold = Long.parseLong(args.get(3));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Threshold is not a valid long: " + args.get(3));
                }

                long sum = getTransactionSum(userId, productType, transactionType);
                return compareValues(sum, threshold, operator);

            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                if (args == null || args.size() < 2
                        || args.get(0) == null || args.get(1) == null) {
                    throw new IllegalArgumentException("Missing arguments for TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW: " + query);
                }
                String prodType = args.get(0);
                String op = args.get(1);

                long deposits = transactionRepository.getTotalDepositsByProductType(userId, prodType);
                long withdrawals = transactionRepository.getTotalExpensesByProductType(userId, prodType);

                return compareValues(deposits, withdrawals, op);

            default:
                throw new IllegalArgumentException("Неизвестный тип запроса: " + queryType);
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
