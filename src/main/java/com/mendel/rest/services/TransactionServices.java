package com.mendel.rest.services;

import com.mendel.rest.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.mendel.rest.exeptions.TransactionAlreadyExistsException;
import com.mendel.rest.models.TransactionModel;

@Service
public class TransactionServices {

    private static final Logger log = LoggerFactory.getLogger(TransactionServices.class);

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionModel createTransaction(TransactionModel transaction) {
        log.info("Created transaction with id: {}", transaction.getTransaction_id());
        return transactionRepository.save(transaction);
    }
    
    public List<TransactionModel> getAllTransactions() {
        log.info("Retrieving all transactions");
        return transactionRepository.findAll();
    }

    public TransactionModel getTransactionById(Long id) {
        Optional<TransactionModel> transactionOpt = transactionRepository.findById(id);
        log.info("Retrieved transaction with id: {}", id);
        return transactionOpt.get();
    }

    public List<TransactionModel> getTransactionsByParentId(Long parentId) {
        List<TransactionModel> transactions = transactionRepository.findAll().stream()
                .filter(t -> parentId.equals(t.getParent_id()))
                .collect(Collectors.toList());
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "No transactions found with parent_id: " + parentId);
        }
        log.info("Found {} transactions with parent_id: {}", transactions.size(), parentId);
        return transactions;
    }

    public List<TransactionModel> getTransactionsByType(String type) {
        log.info("Retrieving transactions of type: {}", type);
        return transactionRepository.findAll().stream()
                .filter(t -> type.equals(t.getType()))
                .collect(Collectors.toList());
    }

    /**
     * Cuenta cuántas transacciones existen del tipo especificado.
     */
    public Long countByType(String type) {
        Long count = transactionRepository.findAll().stream()
                .filter(t -> type.equals(t.getType()))
                .count();
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "No transactions found with type: " + type);
        }
        log.info("Found {} transactions of type: {}", count, type);
        return count;
    }

    public Double getSumByTransactionId(Long transactionId) {
        Optional<TransactionModel> transactionOpt = transactionRepository.findById(transactionId);
        if (transactionOpt.isPresent()) {
            TransactionModel transaction = transactionOpt.get();
            Double sum = transaction.getAmount();
            List<TransactionModel> childTransactions;
            try {
                childTransactions = getTransactionsByParentId(transactionId);
            } catch (ResponseStatusException ex) {
                // no children found, treat as leaf
                childTransactions = List.of();
            }
            for (TransactionModel child : childTransactions) {
                sum += getSumByTransactionId(child.getTransaction_id());
            }
            log.info("Sum for transaction {} is {}", transaction.getTransaction_id(), sum);
            return sum;
        } else {
            log.warn("Transaction with id {} not found", transactionId);
            return 0.0;
        }
    }

    public Double getSumByParentId(Long parentId) {
        List<TransactionModel> transactions = transactionRepository.findAll().stream()
                .filter(t -> parentId.equals(t.getParent_id()))
                .collect(Collectors.toList());
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "No transactions found with parent_id: " + parentId);
        }
        log.info("Found {} transactions with parent_id: {}", transactions.size(), parentId);
        return transactions.stream()
                .mapToDouble(TransactionModel::getAmount)
                .sum();
    }

    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }

    /**
     * Verifica si existe una transacción con el id dado.
     */
    public boolean existsById(Long id) {
        return transactionRepository.existsById(id);
    }

    /**
     * Crea una transacción con el id especificado; si el id ya existe lanza
     * TransactionAlreadyExistsException.
     */
    public TransactionModel saveWithId(Long id, TransactionModel transaction) {
        if (existsById(id)) {
            throw new TransactionAlreadyExistsException(id);
        }
        transaction.setTransaction_id(id);
        log.info("Created transaction with id: {}", transaction.getTransaction_id());
        return transactionRepository.save(transaction);
    }

}
