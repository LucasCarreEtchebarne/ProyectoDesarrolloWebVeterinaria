package metropet_clinic.demo.service;

import metropet_clinic.demo.model.ExpedienteMedico;
import metropet_clinic.demo.repository.ExpedienteMedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExpedienteMedicoService {

    @Autowired
    private ExpedienteMedicoRepository expedienteMedicoRepository;

    public List<ExpedienteMedico> obtenerHistorialPorMascota(Long mascotaId) {
        return expedienteMedicoRepository.findByMascotaId(mascotaId);
    }

    public ExpedienteMedico guardarExpediente(ExpedienteMedico expediente) {
        return expedienteMedicoRepository.save(expediente);
    }
}
