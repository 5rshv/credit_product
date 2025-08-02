package com.example.credit_product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO для условия правила")
public class RuleQueryDTO {
    @Schema(description = "Тип запроса", example = "USER_OF")
    private String query;

    @Schema(description = "Аргументы запроса")
    private List<String> arguments;

    @Schema(description = "Флаг отрицания")
    private Boolean negate;

    // Геттеры и сеттеры
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public Boolean getNegate() {
        return negate;
    }

    public void setNegate(Boolean negate) {
        this.negate = negate;
    }
}