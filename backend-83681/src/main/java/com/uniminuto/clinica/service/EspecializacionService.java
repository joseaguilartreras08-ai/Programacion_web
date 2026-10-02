package com.uniminuto.clinica.service;

import com.uniminuto.clinica.entity.Especializacion;
import com.uniminuto.clinica.exception.BadRequestException;
import com.uniminuto.clinica.models.EspecializacionRq;
import com.uniminuto.clinica.models.MiRespuestaRS;

import java.util.List;

public interface EspecializacionService {

    List<Especializacion> listarEspecializaciones();

    MiRespuestaRS guardarEspecializacion(EspecializacionRq especializacionRq) throws BadRequestException;

    MiRespuestaRS actualizarEspecializacion(EspecializacionRq especializacionRq) throws BadRequestException;

    MiRespuestaRS eliminarEspecializacion(Long id) throws BadRequestException;
}
