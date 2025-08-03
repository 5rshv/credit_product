package com.example.credit_product.repository;

import com.example.credit_product.model.Users;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.ErrorManager;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Users> userRowMapper;

    public UserRepository(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = (rs, rowNum) -> new Users(
                UUID.fromString(rs.getString("id")),
                rs.getString("name")
        );

    }

    /**
     * Проверяет существование пользователя по ID
     */
    public boolean existsById(UUID userId) {
        String sql = "SELECT COUNT(*) > 0 FROM users WHERE id = ?::uuid";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                userId.toString()
        ));
    }


    /**
     * Находит пользователя по ID
     */
    public Optional<Users> findById(UUID userId) {
        String sql = "SELECT id, name FROM users WHERE id = ?";
        try {
            Users user = jdbcTemplate.queryForObject(sql, userRowMapper, userId.toString());
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
    public List<Users> findAllUsersWithTransactions() {
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

    public List<Users> findAll() {
        try {
            String sql = "SELECT id, name FROM users";
            return jdbcTemplate.query(sql, userRowMapper);
        } catch (Exception e) {
            ErrorManager log;
            return List.of(); // Возвращаем пустой список вместо выброса исключения
        }
    }

    public Optional<Users> findByName(String name) {
        String sql = "SELECT id, name FROM users WHERE name = ?";
        try {
            Users user = jdbcTemplate.queryForObject(sql, userRowMapper, name);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }

    }
}