package com.mendel.rest.dtos;

/**
 * DTO para responder con el conteo de transacciones por tipo.
 */
public record TypeCountResponse(String type, Long count) {
}
