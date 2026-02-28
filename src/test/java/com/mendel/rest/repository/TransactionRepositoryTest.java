package com.mendel.rest.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.mendel.rest.models.TransactionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@DisplayName("TransactionRepository Test Suite")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private TransactionModel transaction;

    @BeforeEach
    void setUp() {
        // Clear data before each test
        transactionRepository.deleteAll();
        
        transaction = new TransactionModel();
        transaction.setTransaction_id(1L);
        transaction.setAmount(1000.0);
        transaction.setType("cart");
        transaction.setParent_id(null);
    }

    @Nested
    @DisplayName("Save Transaction Tests")
    class SaveTransactionTests {

        @Test
        @DisplayName("Should save transaction to database")
        void testSaveTransaction() {
            TransactionModel saved = transactionRepository.save(transaction);
            assertNotNull(saved, "Saved transaction should not be null");
            assertEquals(1L, saved.getTransaction_id(), "Transaction ID should match");
            assertEquals(1000.0, saved.getAmount(), "Amount should match");
            assertEquals("cart", saved.getType(), "Type should match");
        }

        @Test
        @DisplayName("Should persist transaction to database")
        void testTransactionPersisted() {
            transactionRepository.save(transaction);
            assertTrue(transactionRepository.existsById(1L), "Transaction should exist in database");
        }
    }

    @Nested
    @DisplayName("Find Transaction Tests")
    class FindTransactionTests {

        @BeforeEach
        void setUpData() {
            transactionRepository.save(transaction);
        }

        @Test
        @DisplayName("Should find transaction by ID")
        void testFindById() {
            Optional<TransactionModel> found = transactionRepository.findById(1L);
            assertTrue(found.isPresent(), "Transaction should exist");
            assertEquals(1L, found.get().getTransaction_id(), "Transaction ID should match");
        }

        @Test
        @DisplayName("Should return empty when transaction not found")
        void testFindByIdNotFound() {
            Optional<TransactionModel> found = transactionRepository.findById(999L);
            assertFalse(found.isPresent(), "Transaction should not exist");
        }

        @Test
        @DisplayName("Should find all transactions")
        void testFindAll() {
            var allTransactions = transactionRepository.findAll();
            assertFalse(allTransactions.isEmpty(), "Should find transactions");
            assertEquals(1, allTransactions.size(), "Should have 1 transaction");
        }
    }

    @Nested
    @DisplayName("Update Transaction Tests")
    class UpdateTransactionTests {

        @BeforeEach
        void setUpData() {
            transactionRepository.save(transaction);
        }

        @Test
        @DisplayName("Should update transaction amount")
        void testUpdateTransactionAmount() {
            transaction.setAmount(2000.0);
            TransactionModel updated = transactionRepository.save(transaction);
            assertEquals(2000.0, updated.getAmount(), "Amount should be updated");
        }

        @Test
        @DisplayName("Should update transaction type")
        void testUpdateTransactionType() {
            transaction.setType("payment");
            TransactionModel updated = transactionRepository.save(transaction);
            assertEquals("payment", updated.getType(), "Type should be updated");
        }
    }

    @Nested
    @DisplayName("Delete Transaction Tests")
    class DeleteTransactionTests {

        @BeforeEach
        void setUpData() {
            transactionRepository.save(transaction);
        }

        @Test
        @DisplayName("Should delete transaction by ID")
        void testDeleteById() {
            transactionRepository.deleteById(1L);
            assertFalse(transactionRepository.existsById(1L), "Transaction should be deleted");
        }

        @Test
        @DisplayName("Should delete transaction entity")
        void testDeleteTransaction() {
            transactionRepository.delete(transaction);
            assertFalse(transactionRepository.existsById(1L), "Transaction should be deleted");
        }
    }

    @Nested
    @DisplayName("Check Existence Tests")
    class CheckExistenceTests {

        @BeforeEach
        void setUpData() {
            transactionRepository.save(transaction);
        }

        @Test
        @DisplayName("Should return true when transaction exists")
        void testExistsByIdTrue() {
            boolean exists = transactionRepository.existsById(1L);
            assertTrue(exists, "Transaction should exist");
        }

        @Test
        @DisplayName("Should return false when transaction does not exist")
        void testExistsByIdFalse() {
            boolean exists = transactionRepository.existsById(999L);
            assertFalse(exists, "Transaction should not exist");
        }
    }

    @Nested
    @DisplayName("Count Transaction Tests")
    class CountTransactionTests {

        @Test
        @DisplayName("Should count empty repository")
        void testCountEmpty() {
            long count = transactionRepository.count();
            assertEquals(0, count, "Repository should be empty");
        }

        @Test
        @DisplayName("Should count transactions in repository")
        void testCountTransactions() {
            transactionRepository.save(transaction);
            long count = transactionRepository.count();
            assertEquals(1, count, "Should count 1 transaction");
        }
    }

    @Nested
    @DisplayName("Multiple Transactions Tests")
    class MultipleTransactionsTests {

        @Test
        @DisplayName("Should save and find multiple transactions")
        void testMultipleTransactions() {
            TransactionModel transaction2 = new TransactionModel();
            transaction2.setTransaction_id(2L);
            transaction2.setAmount(500.0);
            transaction2.setType("item");
            transaction2.setParent_id(1L);
            transactionRepository.save(transaction);
            transactionRepository.save(transaction2);
            assertEquals(2, transactionRepository.count(), "Should have 2 transactions");
            assertTrue(transactionRepository.existsById(1L), "Transaction 1 should exist");
            assertTrue(transactionRepository.existsById(2L), "Transaction 2 should exist");
        }
    }
}
