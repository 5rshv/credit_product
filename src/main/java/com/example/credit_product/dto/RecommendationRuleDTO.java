package com.example.credit_product.dto;

import com.example.credit_product.model.RuleQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class RecommendationRuleDTO {
    private UUID id;
    private String name;
    private String description;
    private String productType;
    private List<RuleQuery> queries;
    private boolean active;
}

