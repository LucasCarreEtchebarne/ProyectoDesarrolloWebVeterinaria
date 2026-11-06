package metropet_clinic.demo.repository;
 
import metropet_clinic.demo.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Integer> {
}
 
