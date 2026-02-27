package com.mendel.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mendel.rest.models.TransactionModel;
import com.mendel.rest.services.TransactionServices;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    @Autowired
    private TransactionServices transactionServices;

    /**
     * Inserta o actualiza una transacción con el ID especificado en la ruta.
     * Path: PUT /transactions/{transaction_id}
     * @param transaction_id El ID de la transacción (en la URL)
     * @param transaction El cuerpo con amount, type, parent_id
     * @return La transacción guardada
     */
    @PutMapping("/{transaction_id}")
    public TransactionModel createTransaction(
            @PathVariable Long transaction_id,
            @RequestBody TransactionModel transaction) {
        transaction.setTransaction_id(transaction_id);
        return transactionServices.createTransaction(transaction);
    }

    /**
     * Return todas las transacciones.
     * Path: GET /transactions
     * @return lista de transacciones.
     */
    @GetMapping("/all-transactions")
    public List<TransactionModel> getAllTransactions() {
        return transactionServices.getAllTransactions();
    }

    @GetMapping("/all-types")
    public String getAlltypes() {
        return new String();
    }
    
    /**
     * Return todas las transacciones del tipo especificado.
     * Path: GET /transactions/types/{type}
     * @param type nombre del tipo de transacción (ej. "cart")
     * @return lista de transacciones coincidentes
     */
    @GetMapping("/types/{type}")
    public List<TransactionModel> getByType(@PathVariable String type) {
        return transactionServices.getTransactionsByType(type);
    }
}
