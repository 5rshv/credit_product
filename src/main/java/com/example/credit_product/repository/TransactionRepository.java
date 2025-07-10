package com.example.credit_product.repository;

import com.example.credit_product.model.Transaction;
import com.example.credit_product.model.TransactionRowMapper;
import com.example.credit_product.model.TransactionType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Transaction> transactionRowMapper;

    public TransactionRepository(JdbcTemplate jdbcTemplate,
                                 TransactionRowMapper transactionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionRowMapper = transactionRowMapper;
    }

    /**
     * Получает общую сумму пополнений по типу продукта для пользователя
     */
    public long getTotalDepositsByProductType(UUID userId, String productTypeCode) {
        String sql = """
                SELECT COALESCE(SUM(t.amount), 0)
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                JOIN product_types pt ON p.type_id = pt.id
                WHERE t.user_id = ? 
                AND pt.code = ?
                AND t.transaction_type_id IN (
                    SELECT id FROM transaction_types WHERE code = 'DEPOSIT'
                )
                """;

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                userId.toString(),
                productTypeCode
        );
    }

    /**
     * Получает общую сумму трат по типу продукта для пользователя
     */
    public long getTotalExpensesByProductType(UUID userId, String productTypeCode) {
        String sql = """
                SELECT COALESCE(SUM(t.amount), 0)
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                JOIN product_types pt ON p.type_id = pt.id
                WHERE t.user_id = ? 
                AND pt.code = ?
                AND t.transaction_type_id IN (
                    SELECT id FROM transaction_types WHERE code = 'WITHDRAWAL'
                )
                """;

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                userId.toString(),
                productTypeCode
        );
    }

    /**
     * Проверяет, есть ли у пользователя транзакции определенного типа
     */
    public boolean hasTransactionsOfType(UUID userId, String transactionTypeCode) {
        String sql = """
                SELECT COUNT(*) > 0
                FROM transactions t
                JOIN transaction_types tt ON t.transaction_type_id = tt.id
                WHERE t.user_id = ? AND tt.code = ?
                """;

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                userId.toString(),
                transactionTypeCode
        ));
    }

    /**
     * Получает все транзакции пользователя по типу продукта
     */
    public List<Transaction> findByUserAndProductType(UUID userId, String productTypeCode) {
        String sql = """
                SELECT 
                    t.id, t.user_id, 
                    p.id as product_id, p.name as product_name,
                    pt.id as product_type_id, pt.code as product_type_code, pt.name as product_type_name,
                    tt.id as transaction_type_id, tt.code as transaction_type_code, tt.name as transaction_type_name,
                    t.amount, t.date
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                JOIN product_types pt ON p.type_id = pt.id
                JOIN transaction_types tt ON t.transaction_type_id = tt.id
                WHERE t.user_id = ? AND pt.code = ?
                """;

        return jdbcTemplate.query(
                sql,
                transactionRowMapper,
                userId.toString(),
                productTypeCode
        );
    }

    /**
     * Получает все типы транзакций из базы данных
     */
    public List<TransactionType> getAllTransactionTypes() {
        String sql = "SELECT id, code, name FROM transaction_types";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new TransactionType(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("code"),
                        rs.getString("name")
                )
        );

    }

    public boolean hasTransactionsOfProductType(UUID userId, String productTypeCode) {
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
}