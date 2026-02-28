package com.mendel.rest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para responder con el conteo de transacciones por tipo.
 */
@Data
@AllArgsConstructor
public class TypeCountResponse {
    private String type;
    private Long count;
}
