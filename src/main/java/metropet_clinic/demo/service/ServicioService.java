package metropet_clinic.demo.service;

import metropet_clinic.demo.model.Servicio;
import metropet_clinic.demo.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    public void guardarServicio(Servicio servicio) {
        servicioRepository.save(servicio);
    }

    public Servicio obtenerServicioPorId(Long id) {
        Optional<Servicio> servicio = servicioRepository.findById(id);
        return servicio.orElse(null);
    }

    public void eliminarServicio(Long id) {
        servicioRepository.deleteById(id);
    }
}