package com.example.credit_product.service;

import com.example.credit_product.dto.CreateRuleRequest;
import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.dto.RecommendationRuleDTO;
import com.example.credit_product.model.RecommendationRule;
import com.example.credit_product.repository.RecommendationRuleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationRuleService {
    private final RecommendationRuleRepository ruleRepository;

    /**
     * Получить все правила
     */
    @Transactional(readOnly = true)
    public List<RecommendationRuleDTO> getAllRules() {
        return ruleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получить активные правила
     */
    @Transactional(readOnly = true)
    public List<RecommendationRuleDTO> getActiveRules() {
        return ruleRepository.findAllByActiveTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получить правило по ID
     */
    @Transactional(readOnly = true)
    public RecommendationRuleDTO getRuleById(UUID id) {
        return ruleRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Правило не найдено: " + id));
    }

    /**
     * Создать новое правило
     */
    @Transactional
    public RecommendationRuleDTO createRule(CreateRuleRequest request) {
        if (ruleRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Правило с таким именем уже существует");
        }

        RecommendationRule rule = RecommendationRule.builder()
                .name(request.getName())
                .description(request.getDescription())
                .productType(request.getProductType())
                .queries(request.getQueries())
                .active(true)
                .build();

        return mapToDTO(ruleRepository.save(rule));
    }

    /**
     * Обновить правило
     */
    @Transactional
    public RecommendationRuleDTO updateRule(UUID id, CreateRuleRequest request) {
        RecommendationRule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Правило не найдено: " + id));

        if (!rule.getName().equals(request.getName()) && ruleRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Правило с таким именем уже существует");
        }

        rule.setName(request.getName());
        rule.setDescription(request.getDescription());
        rule.setProductType(request.getProductType());
        rule.setQueries(request.getQueries());

        return mapToDTO(ruleRepository.save(rule));
    }

    /**
     * Активировать/деактивировать правило
     */
    @Transactional
    public void setRuleActive(UUID id, boolean active) {
        RecommendationRule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Правило не найдено: " + id));

        rule.setActive(active);
        ruleRepository.save(rule);
    }

    /**
     * Удалить правило
     */
    @Transactional
    public void deleteRule(UUID id) {
        if (!ruleRepository.existsById(id)) {
            throw new EntityNotFoundException("Правило не найдено: " + id);
        }
        ruleRepository.deleteById(id);
    }

    /**
     * Получить правила по типу продукта
     */
    @Transactional(readOnly = true)
    public List<RecommendationRuleDTO> getRulesByProductType(String productType) {
        return ruleRepository.findAllByProductType(productType).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получить активные правила по типу продукта
     */
    @Transactional(readOnly = true)
    public List<RecommendationRuleDTO> getActiveRulesByProductType(String productType) {
        return ruleRepository.findAllByProductTypeAndActiveTrue(productType).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private RecommendationRuleDTO mapToDTO(RecommendationRule rule) {
        return RecommendationRuleDTO.builder()
                .id(rule.getId())
                .name(rule.getName())
                .description(rule.getDescription())
                .productType(rule.getProductType())
                .queries(rule.getQueries())
                .active(rule.isActive())
                .build();
    }

    /**
     * Проверить существование правила по ID
     */
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return ruleRepository.existsById(id);
    }

    /**
     * Создать новое правило и вернуть сущность
     */
    @Transactional
    public RecommendationRule createRuleEntity(CreateRuleRequest request) {
        if (ruleRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Правило с таким именем уже существует");
        }

        RecommendationRule rule = RecommendationRule.builder()
                .name(request.getName())
                .description(request.getDescription())
                .productType(request.getProductType())
                .queries(request.getQueries())
                .active(true)
                .build();

        return ruleRepository.save(rule);
    }

}
