
package com.example.credit_product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@Schema(description = "Пользователь банковской системы")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @Schema(description = "Уникальный идентификатор пользователя",
            example = "cd515076-5d8a-44be-930e-8d4fcb79f42d")
    private UUID id;

    @Schema(description = "Имя пользователя", example = "Иван Иванов")
    private String name;
}