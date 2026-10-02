package com.uniminuto.clinica.models;

import lombok.Data;

/**
 * Objeto de entrada para crear o actualizar un raza.
 */
@Data
public class RazaRq {
    private Integer razaId;

    private String nombre;

    private String especie;
}
