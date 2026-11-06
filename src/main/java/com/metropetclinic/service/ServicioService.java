package com.metropetclinic.service;

import com.metropetclinic.domain.Servicio;
import com.metropetclinic.repository.ServicioRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicioService {

    // Se enlaza un objeto ServicioRepository
    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<Servicio> getServicios(boolean activo) {
        if (activo) { // Si solo quiero los servicios activos
            return servicioRepository.findByEstadoTrue();
        } else { // Quiero TODOS los servicios
            return servicioRepository.findAll();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Servicio> getServicio(Integer idServicio) {
        return servicioRepository.findById(idServicio);
    }

    // Si el idServicio esta vacio: se inserta, si tiene algo: se actualiza
    @Transactional
    public void save(Servicio servicio) {
        servicioRepository.save(servicio);
        log.trace("Se guardo el servicio: " + servicio.getNombreServicio());
    }

    @Transactional
    public void delete(Integer idServicio) {
        if (!servicioRepository.existsById(idServicio)) {
            throw new IllegalArgumentException("No existe el servicio: " + idServicio);
        }
        try {
            servicioRepository.deleteById(idServicio);
            servicioRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el servicio, tiene datos asociados");
        }
    }
}