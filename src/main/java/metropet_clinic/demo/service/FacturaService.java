package metropet_clinic.demo.service;

import metropet_clinic.demo.model.Factura;
import metropet_clinic.demo.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    // Se guarda una nueva factura
    public Factura guardarFactura(Factura factura) {
        return facturaRepository.save(factura);
    }

    // Se busca las facturas por id de mascota
    public List<Factura> obtenerFacturasPorMascota(Long mascotaId) {
        return facturaRepository.findByMascotaId(mascotaId);
    }

    // Se obtienen todas las facturas
    public List<Factura> obtenerTodas() {
        return facturaRepository.findAll();
    }
}