package com.mendel.rest.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mendel.rest.exeptions.TransactionAlreadyExistsException;
import com.mendel.rest.models.TransactionModel;
import com.mendel.rest.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionServices Test Suite")
class TransactionServicesTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServices transactionServices;

    private TransactionModel transaction;
    private TransactionModel childTransaction;

    @BeforeEach
    void setUp() {
        transaction = new TransactionModel();
        transaction.setTransaction_id(1L);
        transaction.setAmount(1000.0);
        transaction.setType("cart");
        transaction.setParent_id(null);

        childTransaction = new TransactionModel();
        childTransaction.setTransaction_id(2L);
        childTransaction.setAmount(500.0);
        childTransaction.setType("item");
        childTransaction.setParent_id(1L);
    }

    @Nested
    @DisplayName("Create Transaction Tests")
    class CreateTransactionTests {

        @Test
        @DisplayName("Should create a new transaction successfully")
        void testCreateTransaction() {
            when(transactionRepository.save(any(TransactionModel.class)))
                    .thenReturn(transaction);
            TransactionModel created = transactionServices.createTransaction(transaction);
            assertNotNull(created, "Created transaction should not be null");
            assertEquals(1L, created.getTransaction_id(), "Transaction ID should match");
            assertEquals(1000.0, created.getAmount(), "Amount should match");
            assertEquals("cart", created.getType(), "Type should match");
            verify(transactionRepository, times(1)).save(transaction);
        }

        @Test
        @DisplayName("Should save transaction with ID without conflict")
        void testSaveWithIdSuccess() {
            when(transactionRepository.existsById(1L)).thenReturn(false);
            when(transactionRepository.save(any(TransactionModel.class)))
                    .thenReturn(transaction);
            TransactionModel saved = transactionServices.saveWithId(1L, transaction);
            assertNotNull(saved, "Saved transaction should not be null");
            assertEquals(1L, saved.getTransaction_id(), "Transaction ID should be set");
            verify(transactionRepository, times(1)).existsById(1L);
            verify(transactionRepository, times(1)).save(transaction);
        }

        @Test
        @DisplayName("Should throw exception when saving with existing ID")
        void testSaveWithIdConflict() {
            when(transactionRepository.existsById(1L)).thenReturn(true);
            assertThrows(TransactionAlreadyExistsException.class, () -> {
                transactionServices.saveWithId(1L, transaction);
            }, "Should throw TransactionAlreadyExistsException");
            verify(transactionRepository, times(1)).existsById(1L);
            verify(transactionRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Get Transaction Tests")
    class GetTransactionTests {

        @Test
        @DisplayName("Should return all transactions")
        void testGetAllTransactions() {
            List<TransactionModel> transactions = Arrays.asList(transaction, childTransaction);
            when(transactionRepository.findAll()).thenReturn(transactions);
            List<TransactionModel> result = transactionServices.getAllTransactions();
            assertNotNull(result, "Transaction list should not be null");
            assertEquals(2, result.size(), "Should return 2 transactions");
            assertEquals(1L, result.get(0).getTransaction_id(), "First transaction ID should match");
            verify(transactionRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return transaction by ID")
        void testGetTransactionById() {
            when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
            TransactionModel result = transactionServices.getTransactionById(1L);
            assertNotNull(result, "Transaction should not be null");
            assertEquals(1L, result.getTransaction_id(), "Transaction ID should match");
            verify(transactionRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should throw exception when transaction not found by ID")
        void testGetTransactionByIdNotFound() {
            when(transactionRepository.findById(999L)).thenReturn(Optional.empty());
            assertThrows(Exception.class, () -> {
                transactionServices.getTransactionById(999L);
            }, "Should throw exception when transaction not found");
        }

        @Test
        @DisplayName("Should return child transactions by parent ID")
        void testGetTransactionsByParentId() {
            List<TransactionModel> children = Arrays.asList(childTransaction);
            List<TransactionModel> allTransactions = Arrays.asList(transaction, childTransaction);
            when(transactionRepository.findAll()).thenReturn(allTransactions);
            List<TransactionModel> result = transactionServices.getTransactionsByParentId(1L);
            assertNotNull(result, "Child transactions list should not be null");
            assertEquals(1, result.size(), "Should return 1 child transaction");
            assertEquals(2L, result.get(0).getTransaction_id(), "Child transaction ID should match");
            assertEquals(1L, result.get(0).getParent_id(), "Parent ID should match");
            verify(transactionRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should throw exception when no child transactions found")
        void testGetTransactionsByParentIdNotFound() {
            List<TransactionModel> allTransactions = Arrays.asList(transaction);
            when(transactionRepository.findAll()).thenReturn(allTransactions);
            assertThrows(ResponseStatusException.class, () -> {
                transactionServices.getTransactionsByParentId(999L);
            }, "Should throw exception when no children found");
        }

        @Test
        @DisplayName("Should return transactions by type")
        void testGetTransactionsByType() {
            List<TransactionModel> cartTransactions = Arrays.asList(transaction);
            List<TransactionModel> allTransactions = Arrays.asList(transaction, childTransaction);
            when(transactionRepository.findAll()).thenReturn(allTransactions);
            List<TransactionModel> result = transactionServices.getTransactionsByType("cart");
            assertNotNull(result, "Transactions list should not be null");
            assertEquals(1, result.size(), "Should return 1 cart transaction");
            assertEquals("cart", result.get(0).getType(), "Type should be cart");
        }
    }

    @Nested
    @DisplayName("Count Transaction Tests")
    class CountTransactionTests {

        @Test
        @DisplayName("Should count transactions by type")
        void testCountByType() {
            List<TransactionModel> allTransactions = Arrays.asList(transaction, childTransaction);
            when(transactionRepository.findAll()).thenReturn(allTransactions);
            Long count = transactionServices.countByType("cart");
            assertEquals(1L, count, "Should return count of 1 for 'cart' type");
            verify(transactionRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should throw exception when no transactions of type found")
        void testCountByTypeNotFound() {
            List<TransactionModel> allTransactions = Arrays.asList(transaction);
            when(transactionRepository.findAll()).thenReturn(allTransactions);
            assertThrows(ResponseStatusException.class, () -> {
                transactionServices.countByType("nonexistent");
            }, "Should throw exception when type not found");
        }
    }

    @Nested
    @DisplayName("Sum Transaction Tests")
    class SumTransactionTests {

        @Test
        @DisplayName("Should calculate sum including child transactions")
        void testGetSumByTransactionId() {
            when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
            List<TransactionModel> allTransactions = Arrays.asList(transaction, childTransaction);
            when(transactionRepository.findAll()).thenReturn(allTransactions);
            when(transactionRepository.findById(2L)).thenReturn(Optional.of(childTransaction));
            Double sum = transactionServices.getSumByTransactionId(1L);
            assertEquals(1500.0, sum, "Sum should be 1500.0 (1000 + 500)");
            verify(transactionRepository, atLeast(1)).findById(any());
        }

        @Test
        @DisplayName("Should return amount when transaction has no children")
        void testGetSumByTransactionIdNoChildren() {
            TransactionModel parentOnly = new TransactionModel();
            parentOnly.setTransaction_id(3L);
            parentOnly.setAmount(2000.0);
            parentOnly.setType("sale");
            parentOnly.setParent_id(null);
            
            when(transactionRepository.findById(3L)).thenReturn(Optional.of(parentOnly));
            List<TransactionModel> allTransactions = Arrays.asList(parentOnly);
            when(transactionRepository.findAll()).thenReturn(allTransactions);
            Double sum = transactionServices.getSumByTransactionId(3L);
            assertEquals(2000.0, sum, "Sum should be 2000.0 (no children)");
        }

        @Test
        @DisplayName("Should return 0 when transaction not found")
        void testGetSumByTransactionIdNotFound() {
            when(transactionRepository.findById(999L)).thenReturn(Optional.empty());
            Double sum = transactionServices.getSumByTransactionId(999L);
            assertEquals(0.0, sum, "Sum should be 0.0 when transaction not found");
        }

        @Test
        @DisplayName("Should sum child transactions by parent ID")
        void testGetSumByParentId() {
            List<TransactionModel> allTransactions = Arrays.asList(transaction, childTransaction);
            when(transactionRepository.findAll()).thenReturn(allTransactions);
            Double sum = transactionServices.getSumByParentId(1L);
            assertEquals(500.0, sum, "Sum should be 500.0 (only child)");
            verify(transactionRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should throw exception when no children found for sum")
        void testGetSumByParentIdNotFound() {
            List<TransactionModel> allTransactions = Arrays.asList(transaction);
            when(transactionRepository.findAll()).thenReturn(allTransactions);
            assertThrows(ResponseStatusException.class, () -> {
                transactionServices.getSumByParentId(999L);
            }, "Should throw exception when no children found");
        }
    }

    @Nested
    @DisplayName("Delete and Check Transaction Tests")
    class DeleteAndCheckTests {

        @Test
        @DisplayName("Should delete transaction by ID")
        void testDeleteTransactionById() {
            doNothing().when(transactionRepository).deleteById(1L);
            transactionServices.deleteTransactionById(1L);
            verify(transactionRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should check if transaction exists by ID")
        void testExistsById() {
            when(transactionRepository.existsById(1L)).thenReturn(true);
            boolean exists = transactionServices.existsById(1L);
            assertTrue(exists, "Transaction with ID 1 should exist");
            verify(transactionRepository, times(1)).existsById(1L);
        }

        @Test
        @DisplayName("Should return false when transaction does not exist")
        void testExistsByIdFalse() {
            when(transactionRepository.existsById(999L)).thenReturn(false);
            boolean exists = transactionServices.existsById(999L);
            assertFalse(exists, "Transaction with ID 999 should not exist");
            verify(transactionRepository, times(1)).existsById(999L);
        }
    }
}
