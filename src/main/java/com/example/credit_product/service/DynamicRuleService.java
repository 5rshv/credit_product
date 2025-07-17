package com.example.credit_product.service;

import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.dto.RuleQueryDTO;
import com.example.credit_product.model.DynamicRule;
import com.example.credit_product.repository.DynamicRuleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DynamicRuleService {
    private final DynamicRuleRepository repository;
    private final ObjectMapper objectMapper;

    public DynamicRuleService(DynamicRuleRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public DynamicRuleDTO addRule(DynamicRuleDTO ruleDTO) throws JsonProcessingException {
        DynamicRule rule = new DynamicRule();
        rule.setProductName(ruleDTO.getProductName());
        rule.setProductId(UUID.fromString(ruleDTO.getProductId()));
        rule.setProductText(ruleDTO.getProductText());
        rule.setRuleJson(objectMapper.writeValueAsString(ruleDTO.getRule()));

        DynamicRule saved = repository.save(rule);

        ruleDTO.setId(saved.getId().toString());
        return ruleDTO;
    }

    public List<DynamicRuleDTO> getAllRules() {
//        return StreamSupport.stream(repository.findAll().spliterator(), false)
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
        return null;
    }

    public void deleteRule(UUID id) {
        repository.deleteById(id);
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
            throw new RuntimeException("Error parsing rule JSON", e);
        }
    }
}