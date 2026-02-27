package com.mendel.rest.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class TransactionModel {

    @Id
    private Long transaction_id;
    private Double amount;
    private String type;
    private Long parent_id;
    
}
