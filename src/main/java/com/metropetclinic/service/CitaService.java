package com.metropetclinic.service;

import com.metropetclinic.domain.Cita;
import com.metropetclinic.repository.CitaRepository;
import com.metropetclinic.repository.MascotaRepository;
import com.metropetclinic.repository.ServicioRepository;
import com.metropetclinic.repository.VeterinarioRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CitaService {

    // Se enlazan los repositorios que se ocupan para agendar
    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<Cita> getCitas() {
        return citaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cita> getCita(Integer idCita) {
        return citaRepository.findById(idCita);
    }

    // Agenda de citas (HU06). Si la fecha es null se muestran todas las fechas
    // y si idVeterinario es null se muestran todos los veterinarios.
    @Transactional(readOnly = true)
    public List<Cita> agendaDelDia(Integer idVeterinario, LocalDate fecha) {
        if (fecha == null) {
            if (idVeterinario == null) {
                return citaRepository.findAllByOrderByFechaAscHoraAsc();
            }
            return citaRepository.findByVeterinarioIdVeterinarioOrderByFechaAscHoraAsc(idVeterinario);
        }
        if (idVeterinario == null) {
            return citaRepository.findByFechaOrderByHoraAsc(fecha);
        }
        return citaRepository.findByVeterinarioIdVeterinarioAndFechaOrderByHoraAsc(idVeterinario, fecha);
    }

    // Agendar cita (HU05)
    @Transactional
    public void agendar(Integer idMascota, Integer idVeterinario, Integer idServicio,
            LocalDate fecha, LocalTime hora, String motivo) {
        var mascotaOptional = mascotaRepository.findById(idMascota);
        if (mascotaOptional.isEmpty()) {
            throw new IllegalArgumentException("La mascota seleccionada no existe.");
        }
        var veterinarioOptional = veterinarioRepository.findById(idVeterinario);
        if (veterinarioOptional.isEmpty()) {
            throw new IllegalArgumentException("El veterinario seleccionado no existe.");
        }
        var servicioOptional = servicioRepository.findById(idServicio);
        if (servicioOptional.isEmpty()) {
            throw new IllegalArgumentException("El servicio seleccionado no existe.");
        }

        validarHorario(idVeterinario, fecha, hora, null);

        Cita cita = new Cita();
        cita.setMascota(mascotaOptional.get());
        cita.setVeterinario(veterinarioOptional.get());
        cita.setServicio(servicioOptional.get());
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setMotivo(motivo);
        cita.setEstado("PROGRAMADA");
        citaRepository.save(cita);
    }

    // Cancelar cita (HU11): cambia el estado, NO elimina el registro
    @Transactional
    public void cancelar(Integer idCita) {
        var citaOptional = citaRepository.findById(idCita);
        if (citaOptional.isEmpty()) {
            throw new IllegalArgumentException("La cita no existe: " + idCita);
        }
        Cita cita = citaOptional.get();
        if (!cita.getEstado().equals("PROGRAMADA")) {
            throw new IllegalStateException("Solo se pueden cancelar citas programadas.");
        }
        cita.setEstado("CANCELADA");
        citaRepository.save(cita);
    }

    // Reprogramar cita (HU12): nueva fecha, hora y, si se quiere, otro veterinario
    @Transactional
    public void reprogramar(Integer idCita, Integer idVeterinario, LocalDate fecha, LocalTime hora) {
        var citaOptional = citaRepository.findById(idCita);
        if (citaOptional.isEmpty()) {
            throw new IllegalArgumentException("La cita no existe: " + idCita);
        }
        Cita cita = citaOptional.get();
        if (!cita.getEstado().equals("PROGRAMADA")) {
            throw new IllegalStateException("Solo se pueden reprogramar citas programadas.");
        }
        var veterinarioOptional = veterinarioRepository.findById(idVeterinario);
        if (veterinarioOptional.isEmpty()) {
            throw new IllegalArgumentException("El veterinario seleccionado no existe.");
        }

        validarHorario(idVeterinario, fecha, hora, idCita);

        cita.setVeterinario(veterinarioOptional.get());
        cita.setFecha(fecha);
        cita.setHora(hora);
        citaRepository.save(cita);
    }

    // El sistema debe validar horarios disponibles antes de realizar el cambio
    private void validarHorario(Integer idVeterinario, LocalDate fecha, LocalTime hora, Integer idCita) {
        LocalDate hoy = LocalDate.now();
        if (fecha.isBefore(hoy) || (fecha.isEqual(hoy) && !hora.isAfter(LocalTime.now()))) {
            throw new IllegalStateException("No se puede agendar en una fecha u hora que ya paso.");
        }
        boolean ocupado;
        if (idCita == null) {
            ocupado = citaRepository.existsByVeterinarioIdVeterinarioAndFechaAndHoraAndEstado(
                    idVeterinario, fecha, hora, "PROGRAMADA");
        } else {
            ocupado = citaRepository.existsByVeterinarioIdVeterinarioAndFechaAndHoraAndEstadoAndIdCitaNot(
                    idVeterinario, fecha, hora, "PROGRAMADA", idCita);
        }
        if (ocupado) {
            throw new IllegalStateException("El veterinario no tiene disponible ese horario. Elija otra fecha u hora.");
        }
    }
}