package com.example.credit_product.service.rule;

import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.repository.TransactionRepository;
import com.example.credit_product.service.RecommendationRuleSet;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCredit implements RecommendationRuleSet {
    private static final String PRODUCT_ID = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
    private static final String PRODUCT_NAME = "Простой кредит";
    private static final String DESCRIPTION = "Откройте мир выгодных кредитов с нами!\n\n" +
            "Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! " +
            "Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n\n" +
            "Почему выбирают нас:\n" +
            "- Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n" +
            "- Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n" +
            "- Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.\n\n" +
            "Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";

    private final TransactionRepository transactionRepository;

    public SimpleCredit(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<RecommendationDTO> getRecommendation(UUID userId) {
        // Правило 1: Пользователь не использует продукты с типом CREDIT
        boolean usesCredit = transactionRepository.hasTransactionsOfProductType(userId, "CREDIT");
        if (usesCredit) return Optional.empty();

        // Правило 2: Сумма пополнений по DEBIT > суммы трат по DEBIT
        long debitDeposits = transactionRepository.getTotalDepositsByProductType(userId, "DEBIT");
        long debitExpenses = transactionRepository.getTotalExpensesByProductType(userId, "DEBIT");
        if (debitDeposits <= debitExpenses) return Optional.empty();

        // Правило 3: Сумма трат по DEBIT > 100 000 ₽
        if (debitExpenses <= 100_000) return Optional.empty();

        return Optional.of(new RecommendationDTO(PRODUCT_NAME, PRODUCT_ID, DESCRIPTION));
    }
}