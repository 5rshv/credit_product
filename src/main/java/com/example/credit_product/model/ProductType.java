package com.example.credit_product.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "product_types")
public class ProductType {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code; // например, "SAVING"

    @Column(nullable = false)
    private String name; // например, "Сберегательный счёт"

    public ProductType() {
        // Конструктор без параметров обязателен для JPA
    }

    public ProductType(UUID id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}