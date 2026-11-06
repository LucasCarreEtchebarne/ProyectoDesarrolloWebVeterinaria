package com.metropetclinic.service;

import com.metropetclinic.domain.Vacuna;
import com.metropetclinic.repository.VacunaRepository;
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
public class VacunaService {

    // Se enlaza un objeto VacunaRepository
    private final VacunaRepository vacunaRepository;

    @Transactional(readOnly = true)
    public List<Vacuna> getVacunas() {
        return vacunaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Vacuna> getVacuna(Integer idVacuna) {
        return vacunaRepository.findById(idVacuna);
    }

    // Si el idVacuna esta vacio: se inserta, si tiene algo: se actualiza
    @Transactional
    public void save(Vacuna vacuna) {
        vacunaRepository.save(vacuna);
        log.trace("Se guardo la vacuna: " + vacuna.getIdVacuna());
    }

    @Transactional
    public void delete(Integer idVacuna) {
        if (!vacunaRepository.existsById(idVacuna)) {
            throw new IllegalArgumentException("No existe la vacuna: " + idVacuna);
        }
        try {
            vacunaRepository.deleteById(idVacuna);
            vacunaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la vacuna, tiene datos asociados");
        }
    }
}