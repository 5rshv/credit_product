package com.example.credit_product.controller;

import com.example.credit_product.dto.ErrorResponse;
import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.dto.RecommendationResponse;
import com.example.credit_product.model.Users;
import com.example.credit_product.repository.UserRepository;
import com.example.credit_product.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
@Tag(name = "Рекомендации", description = "API для получения рекомендаций по банковским продуктам")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    public RecommendationController(
            RecommendationService recommendationService,
            UserRepository userRepository) {
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
    }

    @Operation(
            summary = "Получить рекомендации для пользователя",
            description = "Возвращает список рекомендованных продуктов для указанного пользователя",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "UUID пользователя",
                            example = "cd515076-5d8a-44be-930e-8d4fcb79f42d",
                            required = true
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный запрос",
                    content = @Content(
                            schema = @Schema(implementation = RecommendationResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
            )
    })
    @GetMapping("/{userId}")
    public RecommendationResponse getRecommendations(@PathVariable UUID userId) {
        List<RecommendationDTO> recommendations = recommendationService.getRecommendations(userId);
        return new RecommendationResponse(userId.toString(), recommendations);
    }

    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей системы"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный запрос",
                    content = @Content(
                            schema = @Schema(implementation = Users[].class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера"
            )
    })
    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        try {
            List<Users> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/user")
    public List<Users> getAllUser() {
        return userRepository.findAll();
    }


}

