package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Especializacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecializacionRepository extends JpaRepository<Especializacion, Long> {

    List<Especializacion> findAllByOrderByNombreAsc();

    boolean existsByCodigoEspecializacion(String codigoEspecializacion);

    boolean existsByCodigoEspecializacionAndIdNot(String codigoEspecializacion, Long id);

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, Long id);
}
