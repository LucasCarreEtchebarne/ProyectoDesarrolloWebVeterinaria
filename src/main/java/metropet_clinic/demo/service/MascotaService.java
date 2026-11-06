package metropet_clinic.demo.service;

import metropet_clinic.demo.model.Mascota;
import metropet_clinic.demo.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    public List<Mascota> listarMascotas() {
        return mascotaRepository.findAll();
    }

    public void guardarMascota(Mascota mascota) {
        mascotaRepository.save(mascota);
    }

    public Mascota obtenerMascota(Long id) {
        return mascotaRepository.findById(id).orElse(null);
    }

    public List<Mascota> listarPorCliente(Long idCliente) {
        return mascotaRepository.findByClienteIdCliente(idCliente);
    }

    public void eliminarMascota(Long id) {
        mascotaRepository.deleteById(id);
    }
}
