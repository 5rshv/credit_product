package com.example.credit_product.repository;

import com.example.credit_product.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = (rs, rowNum) -> new User(
                UUID.fromString(rs.getString("id")),
                rs.getString("name")
        );
    }

    /**
     * Проверяет существование пользователя по ID
     */
    public boolean existsById(UUID userId) {
        String sql = "SELECT COUNT(*) > 0 FROM users WHERE id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                userId.toString()
        ));
    }

    /**
     * Находит пользователя по ID
     */
    public Optional<User> findById(UUID userId) {
        String sql = "SELECT id, name FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, userId.toString());
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Получает имя пользователя по ID
     */
    public Optional<String> getUserName(UUID userId) {
        String sql = "SELECT name FROM users WHERE id = ?";
        try {
            String name = jdbcTemplate.queryForObject(sql, String.class, userId.toString());
            return Optional.ofNullable(name);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Получает всех пользователей, имеющих транзакции
     */
    public List<User> findAllUsersWithTransactions() {
        String sql = """
            SELECT DISTINCT u.id, u.name
            FROM users u
            JOIN transactions t ON u.id = t.user_id
            """;
        return jdbcTemplate.query(sql, userRowMapper);
    }

    /**
     * Получает количество продуктов, используемых пользователем
     */
    public int countUserProducts(UUID userId) {
        String sql = """
            SELECT COUNT(DISTINCT product_id)
            FROM transactions
            WHERE user_id = ?
            """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId.toString());
    }

    public List<User> findAll() {
        try {
            String sql = "SELECT id, name FROM users";
            return jdbcTemplate.query(sql, (rs, rowNum) ->
                    new User(
                            UUID.fromString(rs.getString("id")),
                            rs.getString("name")
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении пользователей из БД", e);
        }
    }

}