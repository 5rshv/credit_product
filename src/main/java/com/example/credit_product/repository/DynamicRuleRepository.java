package com.example.credit_product.repository;
import com.example.credit_product.model.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {
        @Modifying
        @Query("DELETE FROM DynamicRule dr WHERE dr.productId = :productId")
        void deleteByProductId(@Param("productId") UUID productId);
}

