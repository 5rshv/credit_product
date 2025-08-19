package com.example.credit_product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO для динамического правила рекомендаций")
public class DynamicRuleDTO {
    @Schema(description = "ID правила")
    private String id;

    @Schema(description = "Название продукта", example = "Простой кредит")
    @JsonProperty("product_name")
    private String productName;

    @Schema(description = "ID продукта", example = "ab138afb-f3ba-4a93-b74f-0fcee86d447f")
    @JsonProperty("product_id")
    private String productId;

    @Schema(description = "Текст рекомендации")
    @JsonProperty("product_text")
    private String productText;

    @Schema(description = "Список условий правила")
    private List<RuleQueryDTO> rule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<RuleQueryDTO> getRule() {
        return rule;
    }

    public void setRule(List<RuleQueryDTO> rule) {
        this.rule = rule;
    }
}