package com.example.credit_product.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Product(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                new ProductType(
                        UUID.fromString(rs.getString("type_id")),
                        rs.getString("type_code"),
                        rs.getString("type_name")
                )
        );
    }
}