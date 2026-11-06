package com.metropetclinic.repository;

import com.metropetclinic.domain.Cita;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    // Se crea una consulta "derivada" para ver TODAS las citas ordenadas por fecha y hora
    public List<Cita> findAllByOrderByFechaAscHoraAsc();

    // Se crea una consulta "derivada" para ver todas las citas de un veterinario
    public List<Cita> findByVeterinarioIdVeterinarioOrderByFechaAscHoraAsc(Integer idVeterinario);

    // Se crea una consulta "derivada" para la agenda del día de TODOS los veterinarios
    public List<Cita> findByFechaOrderByHoraAsc(LocalDate fecha);

    // Se crea una consulta "derivada" para la agenda del día de un veterinario
    public List<Cita> findByVeterinarioIdVeterinarioAndFechaOrderByHoraAsc(Integer idVeterinario, LocalDate fecha);

    // Se crea una consulta "derivada" para saber si el veterinario ya tiene una cita a esa hora
    public boolean existsByVeterinarioIdVeterinarioAndFechaAndHoraAndEstado(Integer idVeterinario, LocalDate fecha, LocalTime hora, String estado);

    // Igual que la anterior, pero sin contar la cita que se está reprogramando
    public boolean existsByVeterinarioIdVeterinarioAndFechaAndHoraAndEstadoAndIdCitaNot(Integer idVeterinario, LocalDate fecha, LocalTime hora, String estado, Integer idCita);
}