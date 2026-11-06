package com.metropetclinic.service;

import com.metropetclinic.domain.Factura;
import com.metropetclinic.repository.FacturaRepository;
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
public class FacturaService {

    // Se enlaza un objeto FacturaRepository
    private final FacturaRepository facturaRepository;

    @Transactional(readOnly = true)
    public List<Factura> getFacturas() {
        return facturaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Factura> getFactura(Integer idFactura) {
        return facturaRepository.findById(idFactura);
    }

    // Si el idFactura esta vacio: se inserta, si tiene algo: se actualiza
    @Transactional
    public void save(Factura factura) {
        facturaRepository.save(factura);
        log.trace("Se guardo la factura: " + factura.getIdFactura());
    }

    @Transactional
    public void delete(Integer idFactura) {
        if (!facturaRepository.existsById(idFactura)) {
            throw new IllegalArgumentException("No existe la factura: " + idFactura);
        }
        try {
            facturaRepository.deleteById(idFactura);
            facturaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la factura, tiene datos asociados");
        }
    }
}