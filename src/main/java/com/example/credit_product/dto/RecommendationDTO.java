package com.example.credit_product.dto;

public class RecommendationDTO {
    private String name;
    private String id;
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
