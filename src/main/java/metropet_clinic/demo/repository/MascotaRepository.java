package metropet_clinic.demo.repository;

import metropet_clinic.demo.model.Mascota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByClienteIdCliente(Long idCliente);
}