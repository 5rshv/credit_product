package com.example.credit_product.service;

import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.exception.UserNotFoundException;
import com.example.credit_product.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final List<RecommendationRuleSet> rules;
    private final UserRepository userRepository;

    public RecommendationService(
            List<RecommendationRuleSet> rules,
            UserRepository userRepository) {
        this.rules = rules;
        this.userRepository = userRepository;
    }

    public List<RecommendationDTO> getRecommendations(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId.toString());
        }

        return rules.stream()
                .map(rule -> rule.getRecommendation(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}