package com.uniminuto.clinica.serviceimpl;

import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.entity.Medico;
import com.uniminuto.clinica.exception.BadRequestException;
import com.uniminuto.clinica.models.MedicoRq;
import com.uniminuto.clinica.models.MiRespuestaRS;
import com.uniminuto.clinica.repository.AnotacionHistoriaRepository;
import com.uniminuto.clinica.repository.CitaRepository;
import com.uniminuto.clinica.repository.EspecializacionRepository;
import com.uniminuto.clinica.repository.MedicoRepository;
import com.uniminuto.clinica.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicoServiceImpl implements MedicoService {

    @Autowired
    private  MedicoRepository medicoRepository;

    @Autowired
    private EspecializacionRepository especializacionRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private AnotacionHistoriaRepository anotacionHistoriaRepository;

    @Override
    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    @Override
    public Medico buscarMedico(String tipoDocumento, String numeroDocumento) throws BadRequestException {
        if (numeroDocumento == null || numeroDocumento.isEmpty()) {
            throw new BadRequestException("El número de documento es obligatorio");
        }
        if (tipoDocumento == null || tipoDocumento.isEmpty()) {
            throw new BadRequestException("El tipo de documento es obligatorio");
        }
        Optional<Medico> medico = medicoRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento);
        if (medico.isEmpty()) {
            throw new BadRequestException("Medico no encontrado");
        }
        return medico.get();
    }

    @Override
    public Medico buscarMedicoPorRegistroProfesional(String registroProfesional) throws BadRequestException {
        if (registroProfesional == null || registroProfesional.isEmpty()) {
            throw new BadRequestException("El registro profesional es obligatorio");
        }
        Optional<Medico> medico = medicoRepository.findByRegistroProfesional(registroProfesional);
        if (medico.isEmpty()) {
            throw new BadRequestException("Medico no encontrado");
        }
        return medico.get();
    }

    @Override
    public MiRespuestaRS guardarMedico(MedicoRq medicoRq) throws BadRequestException {
        this.validarObjetoEntrada(medicoRq);
        if (this.medicoRepository.existsByTipoDocumentoAndNumeroDocumento(
                medicoRq.getTipoDocumento(), medicoRq.getNumeroDocumento())) {
            throw new BadRequestException("Ya existe un médico con el documento "
                    + medicoRq.getTipoDocumento() + " " + medicoRq.getNumeroDocumento());
        }
        if (this.medicoRepository.existsByRegistroProfesional(medicoRq.getRegistroProfesional())) {
            throw new BadRequestException("Ya existe un médico con el registro profesional "
                    + medicoRq.getRegistroProfesional());
        }
        Medico medico = new Medico();
        this.copiarDatos(medicoRq, medico);
        this.medicoRepository.save(medico);
        return this.respuesta("Médico guardado correctamente");
    }

    @Override
    public MiRespuestaRS actualizarMedico(MedicoRq medicoRq) throws BadRequestException {
        this.validarObjetoEntrada(medicoRq);
        Medico medico = this.buscarMedicoPorId(medicoRq.getId());
        if (this.medicoRepository.existsByTipoDocumentoAndNumeroDocumentoAndIdNot(
                medicoRq.getTipoDocumento(), medicoRq.getNumeroDocumento(), medico.getId())) {
            throw new BadRequestException("Ya existe otro médico con el documento "
                    + medicoRq.getTipoDocumento() + " " + medicoRq.getNumeroDocumento());
        }
        if (this.medicoRepository.existsByRegistroProfesionalAndIdNot(
                medicoRq.getRegistroProfesional(), medico.getId())) {
            throw new BadRequestException("Ya existe otro médico con el registro profesional "
                    + medicoRq.getRegistroProfesional());
        }
        this.copiarDatos(medicoRq, medico);
        this.medicoRepository.save(medico);
        return this.respuesta("Médico actualizado correctamente");
    }

    @Override
    public MiRespuestaRS eliminarMedico(Long id) throws BadRequestException {
        Medico medico = this.buscarMedicoPorId(id);
        if (this.citaRepository.existsByMedicoId(id)) {
            throw new BadRequestException("No se puede eliminar el médico porque tiene citas registradas");
        }
        if (this.anotacionHistoriaRepository.existsByMedicoId(id)) {
            throw new BadRequestException("No se puede eliminar el médico porque tiene anotaciones en historias médicas");
        }
        this.medicoRepository.delete(medico);
        return this.respuesta("Médico eliminado correctamente");
    }

    private Medico buscarMedicoPorId(Long id) throws BadRequestException {
        if (id == null) {
            throw new BadRequestException("El ID del médico es obligatorio");
        }
        return this.medicoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("El médico con ID " + id + " no existe en la base de datos"));
    }

    private void copiarDatos(MedicoRq medicoRq, Medico medico) throws BadRequestException {
        Especializacion especializacion = this.especializacionRepository.findById(medicoRq.getEspecializacionId())
                .orElseThrow(() -> new BadRequestException("La especialización con ID "
                        + medicoRq.getEspecializacionId() + " no existe en la base de datos"));
        medico.setTipoDocumento(medicoRq.getTipoDocumento());
        medico.setNumeroDocumento(medicoRq.getNumeroDocumento());
        medico.setNombres(medicoRq.getNombres());
        medico.setApellidos(medicoRq.getApellidos());
        medico.setTelefono(medicoRq.getTelefono());
        medico.setRegistroProfesional(medicoRq.getRegistroProfesional());
        medico.setEspecializacion(especializacion);
    }

    private void validarObjetoEntrada(MedicoRq medicoRq) throws BadRequestException {
        if (medicoRq == null) {
            throw new BadRequestException("El objeto de entrada no puede estar vacío");
        }
        medicoRq.setTipoDocumento(limpiar(medicoRq.getTipoDocumento()));
        medicoRq.setNumeroDocumento(limpiar(medicoRq.getNumeroDocumento()));
        medicoRq.setNombres(limpiar(medicoRq.getNombres()));
        medicoRq.setApellidos(limpiar(medicoRq.getApellidos()));
        medicoRq.setTelefono(limpiar(medicoRq.getTelefono()));
        medicoRq.setRegistroProfesional(limpiar(medicoRq.getRegistroProfesional()));
        if (medicoRq.getTipoDocumento() == null) {
            throw new BadRequestException("El tipo de documento es obligatorio");
        }
        if (medicoRq.getNumeroDocumento() == null) {
            throw new BadRequestException("El número de documento es obligatorio");
        }
        if (medicoRq.getNombres() == null) {
            throw new BadRequestException("Los nombres son obligatorios");
        }
        if (medicoRq.getApellidos() == null) {
            throw new BadRequestException("Los apellidos son obligatorios");
        }
        if (medicoRq.getRegistroProfesional() == null) {
            throw new BadRequestException("El registro profesional es obligatorio");
        }
        if (medicoRq.getEspecializacionId() == null) {
            throw new BadRequestException("La especialización es obligatoria");
        }
    }

    private static String limpiar(String texto) {
        return texto == null || texto.trim().isEmpty() ? null : texto.trim();
    }

    private MiRespuestaRS respuesta(String mensaje) {
        MiRespuestaRS respuesta = new MiRespuestaRS();
        respuesta.setStatus(200);
        respuesta.setMessage(mensaje);
        return respuesta;
    }
}
