package com.example.credit_product.controller;

import com.example.credit_product.dto.DynamicRuleDto;
import com.example.credit_product.dto.RuleStatDto;
import com.example.credit_product.service.DynamicRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rule")

public class RuleController {

    private final DynamicRuleService ruleService;

    public RuleController(DynamicRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<Void> createRule(@RequestBody  DynamicRuleDto dto) {
        ruleService.createRule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<DynamicRuleDto>> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable String productId) {
        ruleService.deleteRuleByProductId(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<RuleStatDto>> getStats() {
        return ResponseEntity.ok(ruleService.getRuleStats());
    }
}