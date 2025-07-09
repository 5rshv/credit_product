package com.example.credit_product.repository;

import com.example.credit_product.model.Product;
import com.example.credit_product.model.ProductRowMapper;
import com.example.credit_product.model.ProductType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Product> productRowMapper;

    public ProductRepository(JdbcTemplate jdbcTemplate, ProductRowMapper productRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRowMapper = productRowMapper;
    }

    public boolean userHasProductOfType(UUID userId, String productTypeCode) {
        String sql = """
        SELECT COUNT(*) > 0
        FROM transactions t
        JOIN products p ON t.product_id = p.id
        JOIN product_types pt ON p.type_id = pt.id
        WHERE t.user_id = ? AND pt.code = ?
        """;

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                userId.toString(),
                productTypeCode
        ));
    }

    public List<Product> findProductsByUserAndType(UUID userId, String productTypeCode) {
        String sql = """
            SELECT DISTINCT p.id, p.name, pt.id as type_id, pt.code as type_code, pt.name as type_name
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            JOIN product_types pt ON p.type_id = pt.id
            WHERE t.user_id = ? AND pt.code = ?
            """;

        return jdbcTemplate.query(
                sql,
                productRowMapper,
                userId.toString(),
                productTypeCode
        );
    }

    public Product findById(UUID productId) {
        String sql = """
            SELECT p.id, p.name, pt.id as type_id, pt.code as type_code, pt.name as type_name
            FROM products p
            JOIN product_types pt ON p.type_id = pt.id
            WHERE p.id = ?
            """;

        return jdbcTemplate.queryForObject(
                sql,
                productRowMapper,
                productId.toString()
        );
    }

    public List<ProductType> getAllProductTypes() {
        String sql = "SELECT id, code, name FROM product_types";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ProductType(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("code"),
                        rs.getString("name")
                )
        );
    }
}