package com.example.credit_product.service.rule;

import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.repository.ProductRepository;
import com.example.credit_product.repository.TransactionRepository;
import com.example.credit_product.repository.UserRepository;
import com.example.credit_product.service.RecommendationRuleSet;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500 implements RecommendationRuleSet {
    private static final String PRODUCT_ID = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private static final String PRODUCT_NAME = "Invest 500";
    private static final String DESCRIPTION = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;

    public Invest500(UserRepository userRepository,
                         ProductRepository productRepository,
                         TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<RecommendationDTO> getRecommendation(UUID userId) {
        // Правило 1: Пользователь использует как минимум один продукт с типом DEBIT
        boolean usesDebit = transactionRepository.hasTransactionsOfProductType(userId, "DEBIT");
        if (!usesDebit) return Optional.empty();

        // Правило 2: Пользователь не использует продукты с типом INVEST
        boolean usesInvest = transactionRepository.hasTransactionsOfProductType(userId, "INVEST");
        if (usesInvest) return Optional.empty();

        // Правило 3: Сумма пополнений продуктов с типом SAVING больше 1000 ₽
        long savingDeposits = transactionRepository.getTotalDepositsByProductType(userId, "SAVING");
        if (savingDeposits <= 1000) return Optional.empty();

        return Optional.of(new RecommendationDTO(PRODUCT_NAME, PRODUCT_ID, DESCRIPTION));
    }
}