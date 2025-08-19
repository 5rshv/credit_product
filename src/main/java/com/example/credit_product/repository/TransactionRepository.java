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

    @Cacheable(value = "userProductTypeTransactions", key = "{#userId, #productType}")
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Transaction t " +
            "JOIN t.product p " +
            "JOIN p.type pt " +
            "WHERE t.userId = :userId AND pt.code = :productType")
    boolean hasTransactionsOfProductType(@Param("userId") UUID userId,
                                         @Param("productType") String productType);

    @Cacheable(value = "userProductTypeTransactionsCount", key = "{#userId, #productType}")
    @Query("SELECT COUNT(t) " +
            "FROM Transaction t " +
            "JOIN t.product p " +
            "JOIN p.type pt " +
            "WHERE t.userId = :userId AND pt.code = :productType")
    long countTransactionsByUserAndProductType(@Param("userId") UUID userId,
                                               @Param("productType") String productType);

    @Cacheable(value = "userDepositsByProductType", key = "{#userId, #productType}")
    @Query("SELECT COALESCE(SUM(t.amount), 0) " +
            "FROM Transaction t " +
            "JOIN t.product p " +
            "JOIN p.type pt " +
            "JOIN t.type tt " +
            "WHERE t.userId = :userId AND pt.code = :productType AND tt.code = 'DEPOSIT'")
    long getTotalDepositsByProductType(@Param("userId") UUID userId,
                                       @Param("productType") String productType);

    @Cacheable(value = "userWithdrawalsByProductType", key = "{#userId, #productType}")
    @Query("SELECT COALESCE(SUM(t.amount), 0) " +
            "FROM Transaction t " +
            "JOIN t.product p " +
            "JOIN p.type pt " +
            "JOIN t.type tt " +
            "WHERE t.userId = :userId AND pt.code = :productType AND tt.code = 'WITHDRAWAL'")
    long getTotalExpensesByProductType(@Param("userId") UUID userId,
                                       @Param("productType") String productType);

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

    default void evictCache() {
    }
}