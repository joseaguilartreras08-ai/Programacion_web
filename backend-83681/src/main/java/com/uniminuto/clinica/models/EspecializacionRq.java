package com.uniminuto.clinica.models;

import lombok.Data;

/**
 * Objeto de entrada para crear o actualizar un especialización.
 */
@Data
public class EspecializacionRq {
    private Long id;

    private String nombre;

    private String descripcion;

    private String codigoEspecializacion;
}
