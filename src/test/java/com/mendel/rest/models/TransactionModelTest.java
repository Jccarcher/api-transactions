package com.mendel.rest.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TransactionModel Test Suite")
class TransactionModelTest {

    private TransactionModel transaction;

    @BeforeEach
    void setUp() {
        transaction = new TransactionModel();
    }

    @Nested
    @DisplayName("Creation and Initialization Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create a new transaction with default constructor")
        void testCreateTransactionDefaultConstructor() {
            assertNotNull(transaction, "Transaction should not be null");
            assertNull(transaction.getTransaction_id(), "Transaction ID should be null");
            assertNull(transaction.getAmount(), "Amount should be null");
            assertNull(transaction.getType(), "Type should be null");
            assertNull(transaction.getParent_id(), "Parent ID should be null");
        }

        @Test
        @DisplayName("Should create a new transaction with all fields")
        void testCreateTransactionWithAllFields() {
            transaction.setTransaction_id(1L);
            transaction.setAmount(1000.0);
            transaction.setType("cart");
            transaction.setParent_id(null);
            assertEquals(1L, transaction.getTransaction_id(), "Transaction ID should match");
            assertEquals(1000.0, transaction.getAmount(), "Amount should match");
            assertEquals("cart", transaction.getType(), "Type should match");
            assertNull(transaction.getParent_id(), "Parent ID should be null");
        }
    }

    @Nested
    @DisplayName("Transaction ID Setter and Getter Tests")
    class TransactionIdTests {

        @Test
        @DisplayName("Should set and get transaction ID")
        void testSetAndGetTransactionId() {
            transaction.setTransaction_id(42L);
            assertEquals(42L, transaction.getTransaction_id(), "Transaction ID should match");
        }

        @Test
        @DisplayName("Should handle null transaction ID")
        void testNullTransactionId() {
            transaction.setTransaction_id(null);
            assertNull(transaction.getTransaction_id(), "Transaction ID should be null");
        }
    }

    @Nested
    @DisplayName("Amount Setter and Getter Tests")
    class AmountTests {

        @Test
        @DisplayName("Should set and get amount")
        void testSetAndGetAmount() {
            transaction.setAmount(2500.50);
            assertEquals(2500.50, transaction.getAmount(), "Amount should match");
        }

        @Test
        @DisplayName("Should handle zero amount")
        void testZeroAmount() {
            transaction.setAmount(0.0);
            assertEquals(0.0, transaction.getAmount(), "Amount should be 0");
        }

        @Test
        @DisplayName("Should handle negative amount")
        void testNegativeAmount() {
            transaction.setAmount(-100.0);
            assertEquals(-100.0, transaction.getAmount(), "Amount should be negative");
        }

        @Test
        @DisplayName("Should handle null amount")
        void testNullAmount() {
            transaction.setAmount(null);
            assertNull(transaction.getAmount(), "Amount should be null");
        }
    }

    @Nested
    @DisplayName("Type Setter and Getter Tests")
    class TypeTests {

        @Test
        @DisplayName("Should set and get type")
        void testSetAndGetType() {
            transaction.setType("payment");
            assertEquals("payment", transaction.getType(), "Type should match");
        }

        @Test
        @DisplayName("Should handle different transaction types")
        void testDifferentTypes() {
            // Test cart type
            transaction.setType("cart");
            assertEquals("cart", transaction.getType(), "Type should be cart");

            // Test item type
            transaction.setType("item");
            assertEquals("item", transaction.getType(), "Type should be item");

            // Test purchase type
            transaction.setType("purchase");
            assertEquals("purchase", transaction.getType(), "Type should be purchase");
        }

        @Test
        @DisplayName("Should handle null type")
        void testNullType() {
            transaction.setType(null);
            assertNull(transaction.getType(), "Type should be null");
        }

        @Test
        @DisplayName("Should handle empty string type")
        void testEmptyStringType() {
            transaction.setType("");
            assertEquals("", transaction.getType(), "Type should be empty string");
        }
    }

    @Nested
    @DisplayName("Parent ID Setter and Getter Tests")
    class ParentIdTests {

        @Test
        @DisplayName("Should set and get parent ID")
        void testSetAndGetParentId() {
            transaction.setParent_id(5L);
            assertEquals(5L, transaction.getParent_id(), "Parent ID should match");
        }

        @Test
        @DisplayName("Should handle null parent ID")
        void testNullParentId() {
            transaction.setParent_id(null);
            assertNull(transaction.getParent_id(), "Parent ID should be null");
        }

        @Test
        @DisplayName("Should handle zero parent ID")
        void testZeroParentId() {
            transaction.setParent_id(0L);
            assertEquals(0L, transaction.getParent_id(), "Parent ID should be 0");
        }
    }

    @Nested
    @DisplayName("Complex Scenarios Tests")
    class ComplexScenariosTests {

        @Test
        @DisplayName("Should create parent transaction")
        void testCreateParentTransaction() {
            transaction.setTransaction_id(1L);
            transaction.setAmount(1000.0);
            transaction.setType("cart");
            transaction.setParent_id(null);
            assertNotNull(transaction.getTransaction_id(), "Transaction ID should not be null");
            assertNull(transaction.getParent_id(), "Parent ID should be null for parent transaction");
            assertEquals("cart", transaction.getType(), "Parent should be of type cart");
        }

        @Test
        @DisplayName("Should create child transaction")
        void testCreateChildTransaction() {
            transaction.setTransaction_id(2L);
            transaction.setAmount(500.0);
            transaction.setType("item");
            transaction.setParent_id(1L);
            assertNotNull(transaction.getTransaction_id(), "Transaction ID should not be null");
            assertNotNull(transaction.getParent_id(), "Parent ID should not be null");
            assertEquals(1L, transaction.getParent_id(), "Parent ID should match");
            assertEquals(2L, transaction.getTransaction_id(), "Child ID should match");
        }

        @Test
        @DisplayName("Should update transaction fields")
        void testUpdateTransactionFields() {
            transaction.setTransaction_id(1L);
            transaction.setAmount(100.0);
            transaction.setType("old");
            transaction.setAmount(200.0);
            transaction.setType("new");
            assertEquals(1L, transaction.getTransaction_id(), "ID should remain unchanged");
            assertEquals(200.0, transaction.getAmount(), "Amount should be updated");
            assertEquals("new", transaction.getType(), "Type should be updated");
        }
    }

    @Nested
    @DisplayName("Equality and ToString Tests")
    class EqualsAndToStringTests {

        @Test
        @DisplayName("Should have working toString")
        void testToString() {
            transaction.setTransaction_id(1L);
            transaction.setAmount(1000.0);
            transaction.setType("cart");
            String toString = transaction.toString();
            assertNotNull(toString, "toString should not be null");
            assertTrue(toString.length() > 0, "toString should not be empty");
        }

        @Test
        @DisplayName("Should have equals method (Lombok @Data)")
        void testEqualsMethod() {
            TransactionModel transaction1 = new TransactionModel();
            transaction1.setTransaction_id(1L);
            transaction1.setAmount(1000.0);
            transaction1.setType("cart");

            TransactionModel transaction2 = new TransactionModel();
            transaction2.setTransaction_id(1L);
            transaction2.setAmount(1000.0);
            transaction2.setType("cart");
            assertEquals(transaction1, transaction2, "Transactions with same ID and values should be equal");
        }

        @Test
        @DisplayName("Should have hashCode method (Lombok @Data)")
        void testHashCodeMethod() {
            TransactionModel transaction1 = new TransactionModel();
            transaction1.setTransaction_id(1L);
            TransactionModel transaction2 = new TransactionModel();
            transaction2.setTransaction_id(1L);
            assertEquals(transaction1.hashCode(), transaction2.hashCode(), 
                    "Transactions with same ID should have same hashCode");
        }
    }
}
