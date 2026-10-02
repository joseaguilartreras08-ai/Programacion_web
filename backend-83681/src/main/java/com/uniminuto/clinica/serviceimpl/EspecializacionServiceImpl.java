package com.uniminuto.clinica.serviceimpl;

import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.exception.BadRequestException;
import com.uniminuto.clinica.models.EspecializacionRq;
import com.uniminuto.clinica.models.MiRespuestaRS;
import com.uniminuto.clinica.repository.EspecializacionRepository;
import com.uniminuto.clinica.repository.MedicoRepository;
import com.uniminuto.clinica.service.EspecializacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecializacionServiceImpl implements EspecializacionService {

    @Autowired
    private EspecializacionRepository especializacionRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public List<Especializacion> listarEspecializaciones() {
        return this.especializacionRepository.findAllByOrderByNombreAsc();
    }

    @Override
    public MiRespuestaRS guardarEspecializacion(EspecializacionRq especializacionRq) throws BadRequestException {
        this.validarObjetoEntrada(especializacionRq);
        if (this.especializacionRepository.existsByCodigoEspecializacion(especializacionRq.getCodigoEspecializacion())) {
            throw new BadRequestException("Ya existe una especialización con el código "
                    + especializacionRq.getCodigoEspecializacion());
        }
        if (this.especializacionRepository.existsByNombre(especializacionRq.getNombre())) {
            throw new BadRequestException("Ya existe una especialización con el nombre "
                    + especializacionRq.getNombre());
        }
        Especializacion especializacion = new Especializacion();
        this.copiarDatos(especializacionRq, especializacion);
        this.especializacionRepository.save(especializacion);
        return this.respuesta("Especialización guardada correctamente");
    }

    @Override
    public MiRespuestaRS actualizarEspecializacion(EspecializacionRq especializacionRq) throws BadRequestException {
        this.validarObjetoEntrada(especializacionRq);
        Especializacion especializacion = this.buscarEspecializacion(especializacionRq.getId());
        if (this.especializacionRepository.existsByCodigoEspecializacionAndIdNot(
                especializacionRq.getCodigoEspecializacion(), especializacion.getId())) {
            throw new BadRequestException("Ya existe otra especialización con el código "
                    + especializacionRq.getCodigoEspecializacion());
        }
        if (this.especializacionRepository.existsByNombreAndIdNot(
                especializacionRq.getNombre(), especializacion.getId())) {
            throw new BadRequestException("Ya existe otra especialización con el nombre "
                    + especializacionRq.getNombre());
        }
        this.copiarDatos(especializacionRq, especializacion);
        this.especializacionRepository.save(especializacion);
        return this.respuesta("Especialización actualizada correctamente");
    }

    @Override
    public MiRespuestaRS eliminarEspecializacion(Long id) throws BadRequestException {
        Especializacion especializacion = this.buscarEspecializacion(id);
        if (this.medicoRepository.existsByEspecializacionId(id)) {
            throw new BadRequestException("No se puede eliminar la especialización porque tiene médicos asociados");
        }
        this.especializacionRepository.delete(especializacion);
        return this.respuesta("Especialización eliminada correctamente");
    }

    private Especializacion buscarEspecializacion(Long id) throws BadRequestException {
        if (id == null) {
            throw new BadRequestException("El ID de la especialización es obligatorio");
        }
        return this.especializacionRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(
                        "La especialización con ID " + id + " no existe en la base de datos"));
    }

    private void copiarDatos(EspecializacionRq especializacionRq, Especializacion especializacion) {
        especializacion.setNombre(especializacionRq.getNombre());
        especializacion.setDescripcion(especializacionRq.getDescripcion());
        especializacion.setCodigoEspecializacion(especializacionRq.getCodigoEspecializacion());
    }

    private void validarObjetoEntrada(EspecializacionRq especializacionRq) throws BadRequestException {
        if (especializacionRq == null) {
            throw new BadRequestException("El objeto de entrada no puede estar vacío");
        }
        if (especializacionRq.getNombre() == null || especializacionRq.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre de la especialización no puede estar vacío");
        }
        if (especializacionRq.getCodigoEspecializacion() == null
                || especializacionRq.getCodigoEspecializacion().trim().isEmpty()) {
            throw new BadRequestException("El código de la especialización no puede estar vacío");
        }
        if (especializacionRq.getCodigoEspecializacion().trim().length() > 10) {
            throw new BadRequestException("El código de la especialización no puede tener más de 10 caracteres");
        }
        especializacionRq.setNombre(especializacionRq.getNombre().trim());
        especializacionRq.setCodigoEspecializacion(especializacionRq.getCodigoEspecializacion().trim().toUpperCase());
        if (especializacionRq.getDescripcion() != null) {
            especializacionRq.setDescripcion(especializacionRq.getDescripcion().trim());
        }
    }

    private MiRespuestaRS respuesta(String mensaje) {
        MiRespuestaRS respuesta = new MiRespuestaRS();
        respuesta.setStatus(200);
        respuesta.setMessage(mensaje);
        return respuesta;
    }
}
