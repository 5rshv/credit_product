package com.example.credit_product.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;


import java.util.UUID;

@Table(name = "users")
@Schema(description = "Пользователь банковской системы")
public record User(

       @Id @Schema(description = "Уникальный идентификатор пользователя",
               example = "cd515076-5d8a-44be-930e-8d4fcb79f42d")
       UUID id,
       @Schema(description = "Имя пользователя", example = "Иван Иванов")
        String name
) {}