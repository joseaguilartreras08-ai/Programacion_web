package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.Raza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RazaRepository extends JpaRepository<Raza, Integer> {

    boolean existsByNombreAndEspecie(String nombre, String especie);

    boolean existsByNombreAndEspecieAndRazaIdNot(String nombre, String especie, Integer razaId);
}
