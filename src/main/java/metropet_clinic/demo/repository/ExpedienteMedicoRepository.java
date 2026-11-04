package metropet_clinic.demo.repository;

import metropet_clinic.demo.model.ExpedienteMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpedienteMedicoRepository extends JpaRepository<ExpedienteMedico, Long> {
    
    // Se una un modulo para buscar el id de las mascotas
    List<ExpedienteMedico> findByMascotaId(Long mascotaId);
}