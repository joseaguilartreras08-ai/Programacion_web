package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.ClienteApi;
import com.uniminuto.clinica.entity.Cliente;
import com.uniminuto.clinica.exception.BadRequestException;
import com.uniminuto.clinica.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import com.uniminuto.clinica.models.MiRespuestaRS;
import com.uniminuto.clinica.models.ClienteRq;

@RestController
public class ClienteApiController implements ClienteApi {

    @Autowired
    private ClienteService clienteService;


    @Override
    public ResponseEntity<List<Cliente>> getClientes() throws BadRequestException {
        return ResponseEntity.ok(this.clienteService.getAllClientes());
    }

    @Override
    public ResponseEntity<Cliente> getClientesByNumeroDocumento(String numeroDocumento) throws BadRequestException {
        return ResponseEntity.ok(this.clienteService.getClienteByNumeroDocumento(numeroDocumento));
    }

    @Override
    public ResponseEntity<MiRespuestaRS> guardar(ClienteRq rq) throws BadRequestException {
        return ResponseEntity.ok(this.clienteService.guardarCliente(rq));
    }

    @Override
    public ResponseEntity<MiRespuestaRS> actualizar(ClienteRq rq) throws BadRequestException {
        return ResponseEntity.ok(this.clienteService.actualizarCliente(rq));
    }

    @Override
    public ResponseEntity<MiRespuestaRS> eliminar(Long id) throws BadRequestException {
        return ResponseEntity.ok(this.clienteService.eliminarCliente(id));
    }
}
