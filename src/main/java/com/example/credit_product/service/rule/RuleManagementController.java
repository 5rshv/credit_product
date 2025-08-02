package com.example.credit_product.service.rule;

import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.dto.RuleDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/managment/rule")
public class RuleManagementController {

    private final DynamicRuleInterface ruleService;

    public RuleManagementController(DynamicRuleInterface ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<DynamicRuleDTO> createRule(@RequestBody @Valid DynamicRuleDTO dto) {
        DynamicRuleDTO createdRule = ruleService.createRule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRule);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<DynamicRuleDTO>>> getAllRules() {
        List<DynamicRuleDTO> rules = ruleService.getAllRules();
        return ResponseEntity.ok(Map.of("data", rules));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable String productId) {
        ruleService.deleteRuleByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}
