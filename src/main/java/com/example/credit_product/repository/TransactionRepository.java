package com.example.credit_product.repository;

import com.example.credit_product.model.Transaction;
import com.example.credit_product.model.TransactionType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // Кэшируемый метод для проверки наличия транзакций по типу продукта
    @Cacheable(value = "userProductTypeTransactions", key = "{#userId, #productType}")
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Transaction t " +
            "JOIN t.product p " +
            "JOIN p.type pt " +
            "WHERE t.userId = :userId AND pt.code = :productType")
    boolean hasTransactionsOfProductType(@Param("userId") UUID userId,
                                         @Param("productType") String productType);

    // Кэшируемый метод для подсчета количества транзакций по типу продукта
    @Cacheable(value = "userProductTypeTransactionsCount", key = "{#userId, #productType}")
    @Query("SELECT COUNT(t) " +
            "FROM Transaction t " +
            "JOIN t.product p " +
            "JOIN p.type pt " +
            "WHERE t.userId = :userId AND pt.code = :productType")
    long countTransactionsByUserAndProductType(@Param("userId") UUID userId,
                                               @Param("productType") String productType);

    // Кэшируемый метод для получения суммы пополнений по типу продукта
    @Cacheable(value = "userDepositsByProductType", key = "{#userId, #productType}")
    @Query("SELECT COALESCE(SUM(t.amount), 0) " +
            "FROM Transaction t " +
            "JOIN t.product p " +
            "JOIN p.type pt " +
            "JOIN t.type tt " +
            "WHERE t.userId = :userId AND pt.code = :productType AND tt.code = 'DEPOSIT'")
    long getTotalDepositsByProductType(@Param("userId") UUID userId,
                                       @Param("productType") String productType);

    // Кэшируемый метод для получения суммы трат по типу продукта
    @Cacheable(value = "userWithdrawalsByProductType", key = "{#userId, #productType}")
    @Query("SELECT COALESCE(SUM(t.amount), 0) " +
            "FROM Transaction t " +
            "JOIN t.product p " +
            "JOIN p.type pt " +
            "JOIN t.type tt " +
            "WHERE t.userId = :userId AND pt.code = :productType AND tt.code = 'WITHDRAWAL'")
    long getTotalExpensesByProductType(@Param("userId") UUID userId,
                                       @Param("productType") String productType);

    // Дополнительные методы для работы с транзакциями
    @Cacheable(value = "userTransactions", key = "#userId")
    List<Transaction> findByUserId(UUID userId);

    @Cacheable(value = "transactionsByType", key = "{#userId, #transactionTypeCode}")
    @Query("SELECT t FROM Transaction t " +
            "JOIN t.type tt " +
            "WHERE t.userId = :userId AND tt.code = :transactionTypeCode")
    List<Transaction> findByUserIdAndTransactionType(@Param("userId") UUID userId,
                                                     @Param("transactionTypeCode") String transactionTypeCode);

    @Cacheable(value = "transactionTypes")
    @Query("SELECT tt FROM TransactionType tt")
    List<TransactionType> findAllTransactionTypes();

    // Метод для очистки кэша (вызывать после изменений данных)
    default void evictCache() {
        // В реальной реализации нужно использовать CacheManager для очистки
    }
}