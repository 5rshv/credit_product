package com.example.credit_product.controller;

import com.example.credit_product.dto.CreateRuleRequest;
import com.example.credit_product.dto.RecommendationRuleDTO;
import com.example.credit_product.model.RecommendationRule;
import com.example.credit_product.service.RecommendationRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/recommendation-rules")
@RequiredArgsConstructor
@Validated
public class RecommendationRuleController {
    private final RecommendationRuleService ruleService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Создать новое правило рекомендации")
    @ApiResponse(responseCode = "200", description = "Правило успешно создано")
    @PostMapping
    public ResponseEntity<RecommendationRuleDTO> createRule(@Valid @RequestBody CreateRuleRequest request) {
        RecommendationRule rule = ruleService.createRuleEntity(request);

        return ResponseEntity.ok(convertToDTO(rule));
    }

    @Operation(summary = "Удалить правило рекомендации")
    @ApiResponse(responseCode = "204", description = "Правило успешно удалено")
    @ApiResponse(responseCode = "404", description = "Правило не найдено")
    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID ruleId) {
        if (!ruleService.existsById(ruleId)) {
            return ResponseEntity.notFound().build();
        }
        ruleService.deleteRule(ruleId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить все правила рекомендаций")
    @ApiResponse(responseCode = "200", description = "Список правил успешно получен")
    @GetMapping
    public ResponseEntity<List<RecommendationRuleDTO>> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }

    private RecommendationRuleDTO convertToDTO(RecommendationRule rule) {
        return modelMapper.map(rule, RecommendationRuleDTO.class);
    }
}