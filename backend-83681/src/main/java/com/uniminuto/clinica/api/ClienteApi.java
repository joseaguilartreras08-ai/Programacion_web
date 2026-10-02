package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.Cliente;
import com.uniminuto.clinica.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import com.uniminuto.clinica.models.ClienteRq;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.uniminuto.clinica.models.MiRespuestaRS;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/cliente")
public interface ClienteApi {

    /**
     * Metodo test del servicio.
     *
     * @return Servicio funcionando correctamente.
     * @throws BadRequestException excepcion.
     */
    @GetMapping(value = "/all",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<List<Cliente>> getClientes()
            throws BadRequestException;


    /**
     * Metodo test del servicio.
     *
     * @return Servicio funcionando correctamente.
     * @throws BadRequestException excepcion.
     */
    @GetMapping(value = "/cliente-documento",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<Cliente> getClientesByNumeroDocumento(
            @RequestParam String numeroDocumento
    )
            throws BadRequestException;

    /**
     * Crea un nuevo registro de cliente.
     */
    @PostMapping(value = "/guardar",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<MiRespuestaRS> guardar(
            @RequestBody ClienteRq rq
    )
            throws BadRequestException;

    /**
     * Actualiza un registro de cliente existente.
     */
    @PostMapping(value = "/actualizar",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<MiRespuestaRS> actualizar(
            @RequestBody ClienteRq rq
    )
            throws BadRequestException;

    /**
     * Elimina un registro de cliente por su id.
     */
    @DeleteMapping(value = "/eliminar/{id}",
            produces = {"application/json"})
    ResponseEntity<MiRespuestaRS> eliminar(
            @PathVariable Long id
    )
            throws BadRequestException;
}
