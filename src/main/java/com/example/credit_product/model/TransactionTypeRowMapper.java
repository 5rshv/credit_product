package com.example.credit_product.model;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class TransactionTypeRowMapper implements RowMapper<TransactionType> {
    @Override
    public TransactionType mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TransactionType(
                UUID.fromString(rs.getString("id")),
                rs.getString("code"),
                rs.getString("name")
        );
    }
}