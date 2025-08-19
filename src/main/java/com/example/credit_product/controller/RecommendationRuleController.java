package com.example.credit_product.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.credit_product.dto.CreateRuleRequest;
import com.example.credit_product.dto.RecommendationRuleDTO;
import com.example.credit_product.service.RecommendationRuleService;

@RestController
@RequestMapping("/recommendation-rules")
@RequiredArgsConstructor
public class RecommendationRuleController {

    private final RecommendationRuleService recommendationRuleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecommendationRuleDTO createRule(@RequestBody @Valid CreateRuleRequest request) {
        return recommendationRuleService.createRule(request);
    }

    @DeleteMapping("/{ruleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRule(@PathVariable UUID ruleId) {
        recommendationRuleService.deleteRule(ruleId);
    }

    @GetMapping
    public List<RecommendationRuleDTO> getAllRules() {
        return recommendationRuleService.getAllRules();
    }
}
