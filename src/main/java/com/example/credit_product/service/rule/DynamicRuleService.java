package com.example.credit_product.service.rule;

import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.dto.RuleDto;

import java.util.List;

public interface DynamicRuleService {
    void createRule(DynamicRuleDTO dto);
    List<DynamicRuleDTO> getAllRules();
    void deleteRuleByProductId(String productId);
    List<RuleDto> getRuleStats();

}