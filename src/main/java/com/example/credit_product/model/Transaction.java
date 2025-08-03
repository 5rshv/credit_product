package com.example.credit_product.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions") // Должно совпадать с вашей таблицей в БД
public class Transaction {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private TransactionType type;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false)
    private LocalDate date;

    public Transaction() {
        // обязательный конструктор без параметров
    }

    // Конструктор со всеми полями для удобства
    public Transaction(UUID id, UUID userId, Product product, TransactionType type, long amount, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    // Геттеры и сеттеры

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}