package com.mendel.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mendel.rest.models.TransactionModel;
import com.mendel.rest.services.TransactionServices;
import com.mendel.rest.dtos.SumResponse;
import com.mendel.rest.dtos.TypeCountResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;




@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    @Autowired
    private TransactionServices transactionServices;
    
    /**
     * Return todas las transacciones.
     * Path: GET /transactions
     * @return lista de transacciones.
     */
    @GetMapping("/all-transactions")
    public ResponseEntity<List<TransactionModel>> getAllTransactions() {
        List<TransactionModel> transactions = transactionServices.getAllTransactions();
        if (transactions.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, 
                "No transactions found");
        }
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/all-types")
    public String getAlltypes() {
        return new String();
    }

    /**
     * Inserta o actualiza una transacción con el ID especificado en la ruta.
     * Path: PUT /transactions/{transaction_id}
     * @param transaction_id El ID de la transacción (en la URL)
     * @param transaction El cuerpo con amount, type, parent_id
     * @return La transacción guardada
     */
    @PutMapping("/{transaction_id}")
    public ResponseEntity<?> createTransaction(
            @PathVariable Long transaction_id,
            @RequestBody TransactionModel transaction) {
        try {
            TransactionModel saved = transactionServices.saveWithId(transaction_id, transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (com.mendel.rest.exeptions.TransactionAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }
        /**
     * Return todas las transacciones por id de padre.
     * Path: GET /transactions/{parent_id}
     * @param parent_id id del padre de la transacción (en la URL)
     * @return lista de transacciones coincidentes
     */
    @GetMapping("/{parent_id}")
    public ResponseEntity<List<TransactionModel>> getAlltransactionsByParentId(@PathVariable Long parent_id) {
        List<TransactionModel> transactions = transactionServices.getTransactionsByParentId(parent_id);
        return ResponseEntity.ok(transactions);
    }
            /**
     * Return todas las transacciones por id de padre.
     * Path: GET /transactions/{parent_id}
     * @param parent_id id del padre de la transacción (en la URL)
     * @return lista de transacciones coincidentes
     */
    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<SumResponse> getSumByTransactionId(@PathVariable Long transaction_id) {
        Double sum = transactionServices.getSumByParentId(transaction_id);
        return ResponseEntity.ok(new SumResponse(sum));
    }
    
    /**
     * Return todas las transacciones del tipo especificado.
     * Path: GET /transactions/types/{type}
     * @param type nombre del tipo de transacción (ej. "cart")
     * @return objeto con el tipo y cantidad de transacciones
     */
    @GetMapping("/types/{type}")
    public ResponseEntity<TypeCountResponse> getByType(@PathVariable String type) {
        Long count = transactionServices.countByType(type);
        return ResponseEntity.ok(new TypeCountResponse(type, count));
    }
}
