package com.example.credit_product.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(
                com.example.credit_product.model.RecommendationRule.class,
                com.example.credit_product.dto.RecommendationRuleDTO.class
        );

        return modelMapper;
    }
}
