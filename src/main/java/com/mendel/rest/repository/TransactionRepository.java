package com.mendel.rest.repository;

import com.mendel.rest.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
    
}
