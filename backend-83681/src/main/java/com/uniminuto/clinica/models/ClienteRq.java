package com.uniminuto.clinica.models;

import lombok.Data;
import java.time.LocalDate;

/**
 * Objeto de entrada para crear o actualizar un cliente.
 */
@Data
public class ClienteRq {
    private Long id;

    private String tipoDocumento;

    private String numeroDocumento;

    private String nombres;

    private String apellidos;

    private LocalDate fechaNacimiento;

    private String genero;

    private String telefono;

    private String direccion;

    private Boolean activo;
}
