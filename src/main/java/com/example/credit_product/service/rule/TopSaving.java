package com.example.credit_product.service.rule;


import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.repository.TransactionRepository;
import com.example.credit_product.service.RecommendationRuleSet;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSaving implements RecommendationRuleSet {
    private static final String PRODUCT_ID = "59efc529-2fff-41af-baff-90ccd7402925";
    private static final String PRODUCT_NAME = "Top Saving";
    private static final String DESCRIPTION = "Откройте свою собственную «Копилку» с нашим банком!...";

    private final TransactionRepository transactionRepository;

    public TopSaving(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<RecommendationDTO> getRecommendation(UUID userId) {
        // Правило 1: Пользователь использует как минимум один продукт с типом DEBIT
        boolean usesDebit = transactionRepository.hasTransactionsOfProductType(userId, "DEBIT");
        if (!usesDebit) return Optional.empty();

        // Правило 2: Сумма пополнений по DEBIT >= 50 000 ИЛИ SAVING >= 50 000
        long debitDeposits = transactionRepository.getTotalDepositsByProductType(userId, "DEBIT");
        long savingDeposits = transactionRepository.getTotalDepositsByProductType(userId, "SAVING");
        if (debitDeposits < 50000 && savingDeposits < 50000) return Optional.empty();

        // Правило 3: Сумма пополнений DEBIT > суммы трат DEBIT
        long debitExpenses = transactionRepository.getTotalExpensesByProductType(userId, "DEBIT");
        if (debitDeposits <= debitExpenses) return Optional.empty();

        return Optional.of(new RecommendationDTO(PRODUCT_NAME, PRODUCT_ID, DESCRIPTION));
    }
}