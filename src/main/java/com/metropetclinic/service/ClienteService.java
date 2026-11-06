package com.metropetclinic.service;

import com.metropetclinic.domain.Cliente;
import com.metropetclinic.repository.ClienteRepository;
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
public class ClienteService {

    // Se enlaza un objeto ClienteRepository
    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<Cliente> getClientes() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> getCliente(Integer idCliente) {
        return clienteRepository.findById(idCliente);
    }

    // Si el idCliente esta vacio: se inserta, si tiene algo: se actualiza
    @Transactional
    public void save(Cliente cliente) {
        clienteRepository.save(cliente);
        log.trace("Se guardo el cliente: " + cliente.getIdCliente());
    }

    @Transactional
    public void delete(Integer idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            throw new IllegalArgumentException("No existe el cliente: " + idCliente);
        }
        try {
            clienteRepository.deleteById(idCliente);
            clienteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el cliente, tiene datos asociados");
        }
    }
}