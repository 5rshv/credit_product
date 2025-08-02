package com.example.credit_product.service;

import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.dto.RuleDto;
import com.example.credit_product.dto.RuleQueryDTO;
import com.example.credit_product.model.DynamicRule;
import com.example.credit_product.repository.DynamicRuleRepository;
import com.example.credit_product.service.rule.DynamicRuleInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DynamicRuleService implements DynamicRuleInterface {
    private final DynamicRuleRepository repository;
    private final ObjectMapper objectMapper;

    public DynamicRuleService(DynamicRuleRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public DynamicRuleDTO
    createRule(DynamicRuleDTO dto) {
        try {
            DynamicRule rule = new DynamicRule();
            rule.setProductName(dto.getProductName());
            rule.setProductId(UUID.fromString(dto.getProductId()));
            rule.setProductText(dto.getProductText());
            rule.setRuleJson(objectMapper.writeValueAsString(dto.getRule()));

            DynamicRule savedRule = repository.save(rule);
            dto.setId(savedRule.getId().toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при сериализации правила", e);
        }
        return dto;
    }

    @Override
    public List<DynamicRuleDTO> getAllRules() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRuleByProductId(String productId) {
        repository.deleteByProductId(UUID.fromString(productId));
    }

    @Override
    public List<RuleDto> getRuleStats() {
        // Группируем правила по productId и считаем их количество
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        rule -> rule.getProductId().toString(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> {
                    RuleDto dto = new RuleDto();
                    dto.setProductId(entry.getKey());
                    dto.setTriggeredCount(entry.getValue().intValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    private DynamicRuleDTO convertToDTO(DynamicRule rule) {
        try {
            DynamicRuleDTO dto = new DynamicRuleDTO();
            dto.setId(rule.getId().toString());
            dto.setProductName(rule.getProductName());
            dto.setProductId(rule.getProductId().toString());
            dto.setProductText(rule.getProductText());
            dto.setRule(objectMapper.readValue(rule.getRuleJson(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, RuleQueryDTO.class)));
            return dto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при парсинге JSON правила", e);
        }
    }
}