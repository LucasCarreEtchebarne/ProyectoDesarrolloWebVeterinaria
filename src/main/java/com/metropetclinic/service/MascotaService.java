package com.metropetclinic.service;

import com.metropetclinic.domain.Mascota;
import com.metropetclinic.repository.MascotaRepository;
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
public class MascotaService {

    // Se enlaza un objeto MascotaRepository
    private final MascotaRepository mascotaRepository;

    @Transactional(readOnly = true)
    public List<Mascota> getMascotas() {
        return mascotaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Mascota> getMascota(Integer idMascota) {
        return mascotaRepository.findById(idMascota);
    }

    // Si el idMascota esta vacio: se inserta, si tiene algo: se actualiza
    @Transactional
    public void save(Mascota mascota) {
        mascotaRepository.save(mascota);
        log.trace("Se guardo la mascota: " + mascota.getIdMascota());
    }

    @Transactional
    public void delete(Integer idMascota) {
        if (!mascotaRepository.existsById(idMascota)) {
            throw new IllegalArgumentException("No existe la mascota: " + idMascota);
        }
        try {
            mascotaRepository.deleteById(idMascota);
            mascotaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la mascota, tiene datos asociados");
        }
    }
}