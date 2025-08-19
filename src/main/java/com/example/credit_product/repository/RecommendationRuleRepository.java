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

    List<RecommendationRule> findAllByActiveTrue();

    List<RecommendationRule> findAllByProductType(String productType);

    List<RecommendationRule> findAllByProductTypeAndActiveTrue(String productType);

    boolean existsByName(String name);

    @Modifying
    @Query("UPDATE RecommendationRule r SET r.active = false WHERE r.id = :ruleId")
    void deactivateRule(@Param("ruleId") UUID ruleId);
}