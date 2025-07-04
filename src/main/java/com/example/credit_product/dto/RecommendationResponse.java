package com.example.credit_product.dto;

import java.util.List;

public class RecommendationResponse {
    private String userId;
    private List<RecommendationDTO> recommendations;

    // Конструкторы
    public RecommendationResponse() {
    }

    public RecommendationResponse(String userId, List<RecommendationDTO> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    // Геттеры и сеттеры
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<RecommendationDTO> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<RecommendationDTO> recommendations) {
        this.recommendations = recommendations;
    }
}
