package com.example.credit_product.service.rule;

import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.dto.RuleDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final DynamicRule ruleService;

    public RuleController(DynamicRule ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<Void> createRule(@RequestBody @Valid DynamicRuleDTO dto) {
        ruleService.createRule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<DynamicRuleDTO>> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable String productId) {
        ruleService.deleteRuleByProductId(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<RuleDto>> getStats() {
        return ResponseEntity.ok(ruleService.getRuleStats());
    }
}