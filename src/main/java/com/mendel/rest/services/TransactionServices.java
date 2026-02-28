package com.mendel.rest.services;

import com.mendel.rest.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.mendel.rest.exeptions.TransactionAlreadyExistsException;
import com.mendel.rest.models.TransactionModel;

@Service
public class TransactionServices {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionModel createTransaction(TransactionModel transaction) {
        return transactionRepository.save(transaction);
    }
    
    public List<TransactionModel> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public TransactionModel getTransactionById(Long id) {
        Optional<TransactionModel> transactionOpt = transactionRepository.findById(id);
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
        return transactions;
    }

    public List<TransactionModel> getTransactionsByType(String type) {
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
        return count;
    }

    public Double getSumByTransactionId(Long transactionId) {
        Optional<TransactionModel> transactionOpt = transactionRepository.findById(transactionId);
        if (transactionOpt.isPresent()) {
            TransactionModel transaction = transactionOpt.get();
            Double sum = transaction.getAmount();
            List<TransactionModel> childTransactions = getTransactionsByParentId(transactionId);
            for (TransactionModel child : childTransactions) {
                sum += getSumByTransactionId(child.getTransaction_id());
            }
            return sum;
        } else {
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
        return transactionRepository.save(transaction);
    }

}
