package com.metropetclinic.service;

import com.metropetclinic.domain.ExpedienteMedico;
import com.metropetclinic.repository.ExpedienteMedicoRepository;
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
public class ExpedienteMedicoService {

    // Se enlaza un objeto ExpedienteMedicoRepository
    private final ExpedienteMedicoRepository expedienteMedicoRepository;

    @Transactional(readOnly = true)
    public List<ExpedienteMedico> getExpedientes() {
        return expedienteMedicoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ExpedienteMedico> getExpedienteMedico(Integer idExpediente) {
        return expedienteMedicoRepository.findById(idExpediente);
    }

    // Si el idExpediente esta vacio: se inserta, si tiene algo: se actualiza
    @Transactional
    public void save(ExpedienteMedico expedienteMedico) {
        expedienteMedicoRepository.save(expedienteMedico);
        log.trace("Se guardo el expediente: " + expedienteMedico.getIdExpediente());
    }

    @Transactional
    public void delete(Integer idExpediente) {
        if (!expedienteMedicoRepository.existsById(idExpediente)) {
            throw new IllegalArgumentException("No existe el expediente: " + idExpediente);
        }
        try {
            expedienteMedicoRepository.deleteById(idExpediente);
            expedienteMedicoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el expediente, tiene datos asociados");
        }
    }
}