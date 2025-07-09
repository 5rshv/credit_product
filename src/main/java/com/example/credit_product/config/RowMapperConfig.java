package com.example.credit_product.config;

import com.example.credit_product.model.ProductRowMapper;
import com.example.credit_product.model.ProductTypeRowMapper;
import com.example.credit_product.model.TransactionRowMapper;
import com.example.credit_product.model.TransactionTypeRowMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RowMapperConfig {

    @Bean
    public ProductRowMapper productRowMapper() {
        return new ProductRowMapper();
    }

    @Bean
    public ProductTypeRowMapper productTypeRowMapper() {
        return new ProductTypeRowMapper();
    }

    @Bean
    public TransactionRowMapper transactionRowMapper() {
        return new TransactionRowMapper();
    }

    @Bean
    public TransactionTypeRowMapper transactionTypeRowMapper() {
        return new TransactionTypeRowMapper();
    }
}