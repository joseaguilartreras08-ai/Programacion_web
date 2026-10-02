package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.RazaApi;
import com.uniminuto.clinica.entity.Raza;
import com.uniminuto.clinica.exception.BadRequestException;
import com.uniminuto.clinica.models.MiRespuestaRS;
import com.uniminuto.clinica.models.RazaRq;
import com.uniminuto.clinica.service.RazaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RazaApiController implements RazaApi {

    @Autowired
    private RazaService razaService;

    @Override
    public ResponseEntity<List<Raza>> listar() throws BadRequestException {
        return ResponseEntity.ok(this.razaService.listarRazas());
    }

    @Override
    public ResponseEntity<MiRespuestaRS> guardar(RazaRq rq) throws BadRequestException {
        return ResponseEntity.ok(this.razaService.guardarRaza(rq));
    }

    @Override
    public ResponseEntity<MiRespuestaRS> actualizar(RazaRq rq) throws BadRequestException {
        return ResponseEntity.ok(this.razaService.actualizarRaza(rq));
    }

    @Override
    public ResponseEntity<MiRespuestaRS> eliminar(Integer id) throws BadRequestException {
        return ResponseEntity.ok(this.razaService.eliminarRaza(id));
    }
}
