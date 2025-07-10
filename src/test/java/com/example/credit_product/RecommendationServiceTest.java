package com.example.credit_product;

import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecommendationServiceTest {

    @Autowired
    private RecommendationService recommendationService;

    @Test
    void shouldReturnInvest500ForTestUser() {
        // Подготовка тестовых данных
        UUID testUserId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        String expectedProductId = "147f6a0f-3b91-413b-ab99-87f081d60d5a";

        // Выполнение тестируемого метода
        List<RecommendationDTO> recommendations = recommendationService.getRecommendations(testUserId);

        // Проверка результатов
        assertFalse(recommendations.isEmpty(), "Список рекомендаций не должен быть пустым");
        assertTrue(recommendations.stream()
                        .anyMatch(r -> expectedProductId.equals(r.getId())),
                "Должна быть рекомендация для Invest500");
    }
}