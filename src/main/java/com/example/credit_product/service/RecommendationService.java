package com.example.credit_product.service;

import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.exception.UserNotFoundException;
import com.example.credit_product.model.DynamicRule;
import com.example.credit_product.repository.DynamicRuleRepository;
import com.example.credit_product.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> staticRules;
    private final DynamicRuleService dynamicRuleService;
    private final DynamicRuleEngine dynamicRuleEngine;
    private final UserRepository userRepository;

    public RecommendationService(List<RecommendationRuleSet> staticRules,
                                 DynamicRuleService dynamicRuleService,
                                 DynamicRuleEngine dynamicRuleEngine,
                                 UserRepository userRepository) {
        this.staticRules = staticRules;
        this.dynamicRuleService = dynamicRuleService;
        this.dynamicRuleEngine = dynamicRuleEngine;
        this.userRepository = userRepository;
    }

    public List<RecommendationDTO> getRecommendations(UUID userId) {
        // Проверяем существование пользователя
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId.toString());
        }

        List<RecommendationDTO> recommendations = new ArrayList<>();

        // Применяем статические правила
        for (RecommendationRuleSet rule : staticRules) {
            rule.getRecommendation(userId).ifPresent(recommendations::add);
        }

        // Применяем динамические правила
        List<DynamicRuleDTO> dynamicRules = dynamicRuleService.getAllRules();
        for (DynamicRuleDTO rule : dynamicRules) {
            if (dynamicRuleEngine.evaluateRule(rule, userId)) {
                recommendations.add(new RecommendationDTO(
                        rule.getProductName(),
                        rule.getProductId(),
                        rule.getProductText()
                ));
            }
        }

        return recommendations;
    }
}
