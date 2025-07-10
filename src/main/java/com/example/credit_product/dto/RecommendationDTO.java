package com.example.credit_product.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Рекомендованный банковский продукт")
public class RecommendationDTO {
    @Schema(description = "Название продукта", example = "Invest 500")
    private String name;
    @Schema(description = "Идентификатор продукта", example = "147f6a0f-3b91-413b-ab99-87f081d60d5a")
    private String id;
    @Schema(description = "Описание продукта")
    private String text;

    // Конструкторы
    public RecommendationDTO() {
    }

    public RecommendationDTO(String name, String id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
