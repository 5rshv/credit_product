package com.example.credit_product.model;

import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;


import java.util.UUID;

@Table(schema = "dynamic_rules")
public class DynamicRule {
    @Id
    private UUID id;
    private String productName;
    private UUID productId;
    private String productText;
    private String ruleJson;

    // Конструкторы, геттеры и сеттеры
    public DynamicRule() {
    }

    public DynamicRule(UUID id, String productName, UUID productId, String productText, String ruleJson) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.ruleJson = ruleJson;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public String getRuleJson() {
        return ruleJson;
    }

    public void setRuleJson(String ruleJson) {
        this.ruleJson = ruleJson;
    }
}