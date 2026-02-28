package com.mendel.rest.exeptions;

/**
 * Excepción lanzada cuando se intenta crear una transacción raíz con un ID
 * que ya existe en la base de datos.
 */
public class TransactionAlreadyExistsException extends RuntimeException {
    public TransactionAlreadyExistsException(Long id) {
        super("transaction_id " + id + " already exists; only child records may reference it as parent_id");
    }
}
