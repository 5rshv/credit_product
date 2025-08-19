package com.example.credit_product.controller;

import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.service.DynamicRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новое правило")
    public DynamicRuleDTO createRule(@RequestBody DynamicRuleDTO ruleDTO) {
        return ruleService.createRule(ruleDTO);
    }

    @GetMapping
    @Operation(summary = "Получить все правила")
    public List<DynamicRuleDTO> getAllRules() {
        return ruleService.getAllRules();
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить правило по ID продукта")
    public void deleteRule(@PathVariable String productId) {
        ruleService.deleteRuleByProductId(productId);
    }
}
