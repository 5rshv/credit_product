package com.example.credit_product.service;

import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
        Optional<RecommendationDTO> getRecommendation(UUID userId);

    boolean isApplicable(User user);
    List<RecommendationDTO> generateRecommendations(User user);
}

