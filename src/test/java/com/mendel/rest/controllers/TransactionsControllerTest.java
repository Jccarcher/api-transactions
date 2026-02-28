package com.mendel.rest.controllers;

import static org.junit.jupiter.api.Assertions.*;

import com.mendel.rest.dtos.SumResponse;
import com.mendel.rest.dtos.TypeCountResponse;
import com.mendel.rest.models.TransactionModel;
import com.mendel.rest.repository.TransactionRepository;
import com.mendel.rest.services.TransactionServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@DisplayName("TransactionsController Test Suite (Integration Tests)")
@TestPropertySource(locations = "classpath:application.properties")
class TransactionsControllerTest {

    @Autowired
    private TransactionsController transactionsController;

    @Autowired
    private TransactionServices transactionServices;

    @Autowired
    private TransactionRepository transactionRepository;

    private TransactionModel testTransaction;

    @BeforeEach
    void setUp() {
        // Clear existing transactions via repository
        transactionRepository.deleteAll();

        // Create parent transaction
        testTransaction = new TransactionModel();
        testTransaction.setTransaction_id(100L);
        testTransaction.setAmount(1000.0);
        testTransaction.setType("cart");
        testTransaction.setParent_id(null);
        transactionServices.createTransaction(testTransaction);

        // Add a child transaction to exercise recursive sum logic
        TransactionModel child = new TransactionModel();
        child.setTransaction_id(101L);
        child.setAmount(200.0);
        child.setType("cart");
        child.setParent_id(100L);
        transactionServices.createTransaction(child);
    }

    @Nested
    @DisplayName("Controller Bean Tests")
    class ControllerBeanTests {

        @Test
        @DisplayName("Should have TransactionsController bean available")
        void testControllerBeanExists() {
            assertNotNull(transactionsController, "TransactionsController bean should be autowired");
        }

        @Test
        @DisplayName("Should have TransactionServices bean available")
        void testServicesAvailable() {
            assertNotNull(transactionServices, "TransactionServices bean should be autowired");
        }
    }

    @Nested
    @DisplayName("Transactions Endpoint Tests")
    class TransactionsEndpointTests {

        @Test
        @DisplayName("Should retrieve all transactions")
        void testGetAllTransactions() {
            // Act
            ResponseEntity<List<TransactionModel>> response = transactionsController.getAllTransactions();
            List<TransactionModel> result = response.getBody();

            // Assert
            assertNotNull(result, "Result should not be null");
            assertTrue(result.size() > 0, "Should contain at least one transaction");
        }

        @Test
        @DisplayName("Should count transactions by type")
        void testCountByType() {
            // Act
            ResponseEntity<TypeCountResponse> response = transactionsController.getByType("cart");
            TypeCountResponse result = response.getBody();

            // Assert
            assertNotNull(result, "Result should not be null");
            assertEquals("cart", result.getType());
            assertTrue(result.getCount() > 0);
        }

        @Test
        @DisplayName("Should calculate sum of transactions")
        void testSumTransactions() {
            // Act
            ResponseEntity<SumResponse> response = transactionsController.getSumByTransactionId(100L);
            SumResponse result = response.getBody();

            // Assert
            assertNotNull(result, "Result should not be null");
            // since we created a parent amount 1000 and a child amount 200,
            // the recursive sum should equal 1200
            assertEquals(1200.0, result.getSum(), 0.01, "Sum should correctly include parent and children");
        }
    }
}
