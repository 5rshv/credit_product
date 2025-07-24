package com.example.credit_product.dto;

import com.example.credit_product.model.RuleQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateRuleRequest {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String productType;

    @NotNull
    @Valid
    private List<RuleQuery> queries;
}
