package com.mendel.rest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para responder con la suma de transacciones.
 */
@Data
@AllArgsConstructor
public class SumResponse {
    private Double sum;
}
