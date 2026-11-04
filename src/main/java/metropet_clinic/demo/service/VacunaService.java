package metropet_clinic.demo.service;

import metropet_clinic.demo.model.Vacuna;
import metropet_clinic.demo.repository.VacunaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VacunaService {

    @Autowired
    private VacunaRepository vacunaRepository;

    public List<Vacuna> obtenerVacunasPorMascota(Long mascotaId) {
        return vacunaRepository.findByMascotaId(mascotaId);
    }

    public Vacuna guardarVacuna(Vacuna vacuna) {
        return vacunaRepository.save(vacuna);
    }
}