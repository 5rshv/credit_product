package com.example.credit_product.repository;

import com.example.credit_product.model.RecommendationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecommendationRuleRepository extends JpaRepository<RecommendationRule, UUID> {

    /**
     * Находит все активные правила
     */
    List<RecommendationRule> findAllByActiveTrue();

    /**
     * Находит все правила для определенного типа продукта
     */
    List<RecommendationRule> findAllByProductType(String productType);

    /**
     * Находит все активные правила для определенного типа продукта
     */
    List<RecommendationRule> findAllByProductTypeAndActiveTrue(String productType);

    /**
     * Проверяет существует ли правило с таким именем
     */
    boolean existsByName(String name);

    /**
     * Деактивирует правило по ID
     */
    @Modifying
    @Query("UPDATE RecommendationRule r SET r.active = false WHERE r.id = :ruleId")
    void deactivateRule(@Param("ruleId") UUID ruleId);
}