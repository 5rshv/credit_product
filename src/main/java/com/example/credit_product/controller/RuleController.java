package com.example.credit_product.controller;

import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.service.DynamicRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rule")
@Tag(name = "Динамические правила рекомендаций", description = "API для управления динамическими правилами рекомендаций")
public class RuleController {
    private final DynamicRuleService ruleService;

    public RuleController(DynamicRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    @Operation(summary = "Создать новое правило")
    public ResponseEntity<DynamicRuleDTO> createRule(@RequestBody DynamicRuleDTO ruleDTO) {
        try {
            DynamicRuleDTO createdRule = ruleService.createRule(ruleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRule);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    @Operation(summary = "Получить все правила")
    public ResponseEntity<List<DynamicRuleDTO>> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Удалить правило по ID продукта")
    public ResponseEntity<Void> deleteRule(@PathVariable String productId) {
        ruleService.deleteRuleByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}