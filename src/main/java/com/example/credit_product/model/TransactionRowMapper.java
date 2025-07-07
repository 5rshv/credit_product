package com.example.credit_product.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TransactionRowMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Transaction(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("user_id")),
                new Product(
                        UUID.fromString(rs.getString("product_id")),
                        rs.getString("product_name"),
                        new ProductType(
                                UUID.fromString(rs.getString("product_type_id")),
                                rs.getString("product_type_code"),
                                rs.getString("product_type_name")
                        )
                ),
                new TransactionType(
                        UUID.fromString(rs.getString("transaction_type_id")),
                        rs.getString("transaction_type_code"),
                        rs.getString("transaction_type_name")
                ),
                rs.getLong("amount"),
                rs.getDate("date").toLocalDate()
        );
    }
}