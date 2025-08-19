package com.example.credit_product.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с рекомендациями для пользователя")
public class RecommendationResponse {
    @Schema(description = "Идентификатор пользователя", example = "cd515076-5d8a-44be-930e-8d4fcb79f42d")
    private String userId;

    @Schema(description = "Список рекомендованных продуктов")
    private List<RecommendationDTO> recommendations;


    public RecommendationResponse() {
    }

    public RecommendationResponse(String userId, List<RecommendationDTO> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

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
