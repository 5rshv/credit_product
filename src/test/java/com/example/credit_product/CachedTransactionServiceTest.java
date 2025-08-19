package com.example.credit_product;

import com.example.credit_product.repository.TransactionRepository;
import com.example.credit_product.service.CachedTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CachedTransactionServiceTest {

    private TransactionRepository transactionRepository;
    private CachedTransactionService service;
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        service = new CachedTransactionService(transactionRepository);
    }

    @Test
    void hasTransactionsOfProductType_cachesByUserAndType() {
        when(transactionRepository.hasTransactionsOfProductType(userId, "CARD")).thenReturn(true);

        boolean a = service.hasTransactionsOfProductType(userId, "CARD");
        boolean b = service.hasTransactionsOfProductType(userId, "CARD");

        assertEquals(true, a);
        assertEquals(true, b);
        verify(transactionRepository, times(1)).hasTransactionsOfProductType(userId, "CARD");
    }

    @Test
    void getTotalDepositsByProductType_cachesByUserAndType() {
        when(transactionRepository.getTotalDepositsByProductType(userId, "CARD")).thenReturn(500L);

        long a = service.getTotalDepositsByProductType(userId, "CARD");
        long b = service.getTotalDepositsByProductType(userId, "CARD");

        assertEquals(500L, a);
        assertEquals(500L, b);
        verify(transactionRepository, times(1)).getTotalDepositsByProductType(userId, "CARD");
    }

    @Test
    void getTotalExpensesByProductType_cachesByUserAndType() {
        when(transactionRepository.getTotalExpensesByProductType(userId, "CARD")).thenReturn(300L);

        long a = service.getTotalExpensesByProductType(userId, "CARD");
        long b = service.getTotalExpensesByProductType(userId, "CARD");

        assertEquals(300L, a);
        assertEquals(300L, b);
        verify(transactionRepository, times(1)).getTotalExpensesByProductType(userId, "CARD");
    }
}
