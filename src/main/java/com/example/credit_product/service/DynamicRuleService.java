package com.example.credit_product.service;


import com.example.credit_product.dto.DynamicRuleDto;
import com.example.credit_product.dto.RuleStatDto;

import java.util.List;

public interface DynamicRuleService {
    void createRule(DynamicRuleDto dto);
    List<DynamicRuleDto> getAllRules();
    void deleteRuleByProductId(String productId);
    List<RuleStatDto> getRuleStats();
}