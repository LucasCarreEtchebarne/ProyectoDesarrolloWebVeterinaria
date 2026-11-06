package com.metropetclinic.repository;

import com.metropetclinic.domain.Servicio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    // Se crea una consulta "derivada" para recuperar los servicios ACTIVOS
    public List<Servicio> findByEstadoTrue();
}