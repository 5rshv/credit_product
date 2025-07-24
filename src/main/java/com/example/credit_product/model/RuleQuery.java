package com.example.credit_product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleQuery {
    private String query;
    private List<String> arguments;
    private boolean negate;
}
