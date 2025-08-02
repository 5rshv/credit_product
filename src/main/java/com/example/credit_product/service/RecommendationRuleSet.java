package com.example.credit_product.service;

import com.example.credit_product.dto.RecommendationDTO;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
        Optional<RecommendationDTO> getRecommendation(UUID userId);
    }

