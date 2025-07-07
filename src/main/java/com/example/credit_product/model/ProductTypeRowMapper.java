package com.example.credit_product.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProductTypeRowMapper implements RowMapper<ProductType> {
    @Override
    public ProductType mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductType(
                UUID.fromString(rs.getString("id")),
                rs.getString("code"),
                rs.getString("name")
        );
    }
}