package com.uniminuto.clinica.serviceimpl;

import com.uniminuto.clinica.entity.Raza;
import com.uniminuto.clinica.exception.BadRequestException;
import com.uniminuto.clinica.models.MiRespuestaRS;
import com.uniminuto.clinica.models.RazaRq;
import com.uniminuto.clinica.repository.MascotaRepository;
import com.uniminuto.clinica.repository.RazaRepository;
import com.uniminuto.clinica.service.RazaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RazaServiceImpl implements RazaService {

    @Autowired
    private RazaRepository razaRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Override
    public List<Raza> listarRazas() {
        return this.razaRepository.findAll(Sort.by("especie", "nombre"));
    }

    @Override
    public MiRespuestaRS guardarRaza(RazaRq razaRq) throws BadRequestException {
        this.validarObjetoEntrada(razaRq);
        if (this.razaRepository.existsByNombreAndEspecie(razaRq.getNombre(), razaRq.getEspecie())) {
            throw new BadRequestException("Ya existe la raza " + razaRq.getNombre()
                    + " para la especie " + razaRq.getEspecie());
        }
        Raza raza = new Raza();
        raza.setNombre(razaRq.getNombre());
        raza.setEspecie(razaRq.getEspecie());
        raza.setFechaCreacion(LocalDateTime.now());
        this.razaRepository.save(raza);
        return this.respuesta("Raza guardada correctamente");
    }

    @Override
    public MiRespuestaRS actualizarRaza(RazaRq razaRq) throws BadRequestException {
        this.validarObjetoEntrada(razaRq);
        Raza raza = this.buscarRaza(razaRq.getRazaId());
        if (this.razaRepository.existsByNombreAndEspecieAndRazaIdNot(
                razaRq.getNombre(), razaRq.getEspecie(), raza.getRazaId())) {
            throw new BadRequestException("Ya existe otra raza " + razaRq.getNombre()
                    + " para la especie " + razaRq.getEspecie());
        }
        raza.setNombre(razaRq.getNombre());
        raza.setEspecie(razaRq.getEspecie());
        raza.setFechaModificacion(LocalDateTime.now());
        this.razaRepository.save(raza);
        return this.respuesta("Raza actualizada correctamente");
    }

    @Override
    public MiRespuestaRS eliminarRaza(Integer razaId) throws BadRequestException {
        Raza raza = this.buscarRaza(razaId);
        if (this.mascotaRepository.existsByRazaRazaId(razaId)) {
            throw new BadRequestException("No se puede eliminar la raza porque tiene mascotas registradas");
        }
        this.razaRepository.delete(raza);
        return this.respuesta("Raza eliminada correctamente");
    }

    private Raza buscarRaza(Integer razaId) throws BadRequestException {
        if (razaId == null) {
            throw new BadRequestException("El ID de la raza es obligatorio");
        }
        return this.razaRepository.findById(razaId)
                .orElseThrow(() -> new BadRequestException("La raza con ID " + razaId + " no existe en la base de datos"));
    }

    private void validarObjetoEntrada(RazaRq razaRq) throws BadRequestException {
        if (razaRq == null) {
            throw new BadRequestException("El objeto de entrada no puede estar vacío");
        }
        if (razaRq.getNombre() == null || razaRq.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre de la raza no puede estar vacío");
        }
        if (razaRq.getEspecie() == null || razaRq.getEspecie().trim().isEmpty()) {
            throw new BadRequestException("La especie no puede estar vacía");
        }
        razaRq.setNombre(razaRq.getNombre().trim());
        razaRq.setEspecie(razaRq.getEspecie().trim());
    }

    private MiRespuestaRS respuesta(String mensaje) {
        MiRespuestaRS respuesta = new MiRespuestaRS();
        respuesta.setStatus(200);
        respuesta.setMessage(mensaje);
        return respuesta;
    }
}
