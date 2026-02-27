package com.mendel.rest.services;

import com.mendel.rest.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<TransactionModel> getTransactionsByType(String type) {
        return transactionRepository.findAll().stream()
                .filter(t -> type.equals(t.getType()))
                .collect(Collectors.toList());
    }

    public Double getSumByParentId(Long parentId) {
        return transactionRepository.findAll().stream()
                .filter(t -> parentId.equals(t.getParent_id()))
                .mapToDouble(TransactionModel::getAmount)
                .sum();
    }

    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }

}
