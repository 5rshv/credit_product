package com.example.credit_product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "recommendation_rules",
        indexes = {
                @Index(name = "idx_recommendation_rules_name", columnList = "name", unique = true),
                @Index(name = "idx_recommendation_rules_active_product", columnList = "active,product_type")
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Имя правила не может быть пустым")
    @Size(max = 255, message = "Имя правила не может быть длиннее 255 символов")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Описание правила не может быть пустым")
    @Column(nullable = false)
    private String description;

    @NotBlank(message = "Тип продукта не может быть пустым")
    @Column(nullable = false)
    private String productType;

    @NotNull(message = "Список запросов не может быть пустым")
    @Column(columnDefinition = "jsonb", nullable = false)
    @Convert(converter = JsonConverter.class)
    private List<RuleQuery> queries;

    @Column(nullable = false)
    private boolean active = true;
}

