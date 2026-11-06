package metropet_clinic.demo.repository;
 
import java.time.LocalDate;
import java.util.List;
import metropet_clinic.demo.model.Cita;
import metropet_clinic.demo.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
 
    //Se crea una consulta "derivada" para la agenda del día de TODOS los veterinarios (recepción)
    public List<Cita> findByFechaOrderByHoraAsc(LocalDate fecha);
 
    //Se crea una consulta "derivada" para la agenda del día de un veterinario 
    public List<Cita> findByVeterinarioIdVeterinarioAndFechaOrderByHoraAsc(Integer idVeterinario, LocalDate fecha);
 
    //Se crea una consulta "derivada" para saber qué horas ya están ocupadas
    public List<Cita> findByVeterinarioIdVeterinarioAndFechaAndEstado(Integer idVeterinario, LocalDate fecha, EstadoCita estado);
}
