package com.uniminuto.clinica.serviceimpl;

import com.uniminuto.clinica.entity.Cliente;
import com.uniminuto.clinica.exception.BadRequestException;
import com.uniminuto.clinica.models.ClienteRq;
import com.uniminuto.clinica.models.MiRespuestaRS;
import com.uniminuto.clinica.repository.ClienteRepository;
import com.uniminuto.clinica.repository.MascotaRepository;
import com.uniminuto.clinica.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Override
    public List<Cliente> getAllClientes() {
        return this.clienteRepository.findAll();
    }

    @Override
    public Cliente getClienteByNumeroDocumento(String numeroDocumento)
            throws BadRequestException {
        if (numeroDocumento == null || numeroDocumento.isEmpty()) {
            throw new BadRequestException("El número de documento no puede ser nulo o vacío");
        }
        Optional<Cliente> optCliente = this.clienteRepository.findByNumeroDocumento(numeroDocumento);
        if (optCliente.isEmpty()) {
            throw new BadRequestException("Cliente no encontrado");
        }
        return optCliente.get();
    }

    @Override
    public MiRespuestaRS guardarCliente(ClienteRq clienteRq) throws BadRequestException {
        this.validarObjetoEntrada(clienteRq);
        if (this.clienteRepository.existsByTipoDocumentoAndNumeroDocumento(
                clienteRq.getTipoDocumento(), clienteRq.getNumeroDocumento())) {
            throw new BadRequestException("Ya existe un cliente con el documento "
                    + clienteRq.getTipoDocumento() + " " + clienteRq.getNumeroDocumento());
        }
        Cliente cliente = new Cliente();
        this.copiarDatos(clienteRq, cliente);
        this.clienteRepository.save(cliente);
        return this.respuesta("Cliente guardado correctamente");
    }

    @Override
    public MiRespuestaRS actualizarCliente(ClienteRq clienteRq) throws BadRequestException {
        this.validarObjetoEntrada(clienteRq);
        Cliente cliente = this.buscarCliente(clienteRq.getId());
        if (this.clienteRepository.existsByTipoDocumentoAndNumeroDocumentoAndIdNot(
                clienteRq.getTipoDocumento(), clienteRq.getNumeroDocumento(), cliente.getId())) {
            throw new BadRequestException("Ya existe otro cliente con el documento "
                    + clienteRq.getTipoDocumento() + " " + clienteRq.getNumeroDocumento());
        }
        this.copiarDatos(clienteRq, cliente);
        this.clienteRepository.save(cliente);
        return this.respuesta("Cliente actualizado correctamente");
    }

    @Override
    public MiRespuestaRS eliminarCliente(Long id) throws BadRequestException {
        Cliente cliente = this.buscarCliente(id);
        if (this.mascotaRepository.existsByClienteId(id)) {
            throw new BadRequestException("No se puede eliminar el cliente porque tiene mascotas registradas");
        }
        this.clienteRepository.delete(cliente);
        return this.respuesta("Cliente eliminado correctamente");
    }

    private Cliente buscarCliente(Long id) throws BadRequestException {
        if (id == null) {
            throw new BadRequestException("El ID del cliente es obligatorio");
        }
        return this.clienteRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("El cliente con ID " + id + " no existe en la base de datos"));
    }

    private void copiarDatos(ClienteRq clienteRq, Cliente cliente) {
        cliente.setTipoDocumento(clienteRq.getTipoDocumento());
        cliente.setNumeroDocumento(clienteRq.getNumeroDocumento());
        cliente.setNombres(clienteRq.getNombres());
        cliente.setApellidos(clienteRq.getApellidos());
        cliente.setFechaNacimiento(clienteRq.getFechaNacimiento());
        cliente.setGenero(clienteRq.getGenero());
        cliente.setTelefono(clienteRq.getTelefono());
        cliente.setDireccion(clienteRq.getDireccion());
        cliente.setActivo(clienteRq.getActivo() == null || clienteRq.getActivo());
    }

    private void validarObjetoEntrada(ClienteRq clienteRq) throws BadRequestException {
        if (clienteRq == null) {
            throw new BadRequestException("El objeto de entrada no puede estar vacío");
        }
        clienteRq.setTipoDocumento(limpiar(clienteRq.getTipoDocumento()));
        clienteRq.setNumeroDocumento(limpiar(clienteRq.getNumeroDocumento()));
        clienteRq.setNombres(limpiar(clienteRq.getNombres()));
        clienteRq.setApellidos(limpiar(clienteRq.getApellidos()));
        clienteRq.setGenero(limpiar(clienteRq.getGenero()));
        clienteRq.setTelefono(limpiar(clienteRq.getTelefono()));
        clienteRq.setDireccion(limpiar(clienteRq.getDireccion()));
        if (clienteRq.getTipoDocumento() == null) {
            throw new BadRequestException("El tipo de documento no puede estar vacío");
        }
        if (clienteRq.getNumeroDocumento() == null) {
            throw new BadRequestException("El número de documento no puede estar vacío");
        }
        if (clienteRq.getNombres() == null) {
            throw new BadRequestException("Los nombres no pueden estar vacíos");
        }
        if (clienteRq.getApellidos() == null) {
            throw new BadRequestException("Los apellidos no pueden estar vacíos");
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
