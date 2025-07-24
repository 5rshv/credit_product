package com.example.credit_product.service;

import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.exception.UserNotFoundException;
import com.example.credit_product.model.DynamicRule;
import com.example.credit_product.model.RecommendationRule;
import com.example.credit_product.model.User;
import com.example.credit_product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final List<RecommendationRuleSet> staticRuleSets;
    private final RecommendationRuleService ruleService;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<RecommendationDTO> getRecommendations(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + userId));

        List<RecommendationDTO> recommendations = new ArrayList<>();

        // Применяем статические правила
        for (RecommendationRuleSet ruleSet : staticRuleSets) {
            if (ruleSet.isApplicable(user)) {
                recommendations.addAll(ruleSet.generateRecommendations(user));
            }
        }

        // Применяем динамические правила
        List<RecommendationRule> dynamicRules = ruleService.getAllRules();
        for (RecommendationRule rule : dynamicRules) {
            if (rule.isActive() && ruleService.evaluateRule(rule, userId)) {
                recommendations.add(createRecommendationFromRule(rule));
            }
        }

        return recommendations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private RecommendationDTO createRecommendationFromRule(RecommendationRule rule) {
        return RecommendationDTO.builder()
                .id(UUID.randomUUID())
                .title(rule.getName())
                .description(rule.getDescription())
                .type(rule.getProductType())
                .build();
    }
}

