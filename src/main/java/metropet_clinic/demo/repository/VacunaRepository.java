
 package metropet_clinic.demo.repository;

import metropet_clinic.demo.model.Vacuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VacunaRepository extends JpaRepository<Vacuna, Long> {
    
    // Método para consultar el historial de vacunas de una mascota específica
    List<Vacuna> findByMascotaId(Long mascotaId);
}   

