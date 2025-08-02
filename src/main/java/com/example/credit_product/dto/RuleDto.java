package com.example.credit_product.dto;

public class RuleDto {
    private String productId;
    private int triggeredCount;

    public String getProductId() {
        return productId;
    }

    public int getTriggeredCount() {
        return triggeredCount;
    }

    public void setTriggeredCount(int triggeredCount) {
        this.triggeredCount = triggeredCount;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}