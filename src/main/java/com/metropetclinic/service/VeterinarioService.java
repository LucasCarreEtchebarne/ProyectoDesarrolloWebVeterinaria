package com.metropetclinic.service;

import com.metropetclinic.domain.Veterinario;
import com.metropetclinic.repository.VeterinarioRepository;
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
public class VeterinarioService {

    // Se enlaza un objeto VeterinarioRepository
    private final VeterinarioRepository veterinarioRepository;

    @Transactional(readOnly = true)
    public List<Veterinario> getVeterinarios() {
        return veterinarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Veterinario> getVeterinario(Integer idVeterinario) {
        return veterinarioRepository.findById(idVeterinario);
    }

    // Si el idVeterinario esta vacio: se inserta, si tiene algo: se actualiza
    @Transactional
    public void save(Veterinario veterinario) {
        veterinarioRepository.save(veterinario);
        log.trace("Se guardo el veterinario: " + veterinario.getIdVeterinario());
    }

    @Transactional
    public void delete(Integer idVeterinario) {
        if (!veterinarioRepository.existsById(idVeterinario)) {
            throw new IllegalArgumentException("No existe el veterinario: " + idVeterinario);
        }
        try {
            veterinarioRepository.deleteById(idVeterinario);
            veterinarioRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el veterinario, tiene datos asociados");
        }
    }
}