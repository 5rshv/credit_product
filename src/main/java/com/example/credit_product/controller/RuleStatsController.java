package com.example.credit_product.controller;

import com.example.credit_product.dto.RuleDto;
import com.example.credit_product.service.DynamicRuleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class RuleStatsController {
    private final DynamicRuleService dynamicRuleService;

    public RuleStatsController(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
    }

    @GetMapping("/rule/stats")
    public List<RuleDto> getStats() {
        return dynamicRuleService.getRuleStats();
    }
}