package com.metropetclinic.service;

import com.metropetclinic.domain.Cita;
import com.metropetclinic.domain.EstadoCita;
import com.metropetclinic.domain.Mascota;
import com.metropetclinic.domain.Servicio;
import com.metropetclinic.domain.Veterinario;
import com.metropetclinic.repository.CitaRepository;
import com.metropetclinic.repository.MascotaRepository;
import com.metropetclinic.repository.ServicioRepository;
import com.metropetclinic.repository.VeterinarioRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CitaService {

    // Horario de atención: citas de 30 min entre las 8:00 y las 17:00
    public static final LocalTime HORA_INICIO = LocalTime.of(8, 0);
    public static final LocalTime HORA_FIN = LocalTime.of(17, 0);
    public static final int DURACION_MINUTOS = 30;

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

    // Horas libres de un veterinario en una fecha.
    // idCitaExcluida sirve al reprogramar: la hora que ya ocupa esa cita cuenta como libre.
    @Transactional(readOnly = true)
    public List<LocalTime> horasDisponibles(Integer idVeterinario, LocalDate fecha, Integer idCitaExcluida) {
        List<LocalTime> libres = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        if (fecha.isBefore(hoy)) {
            return libres;
        }
        Set<LocalTime> ocupadas = citaRepository
                .findByVeterinarioIdVeterinarioAndFechaAndEstado(idVeterinario, fecha, EstadoCita.PROGRAMADA)
                .stream()
                .filter(c -> !c.getIdCita().equals(idCitaExcluida))
                .map(Cita::getHora)
                .collect(Collectors.toSet());
        boolean esHoy = fecha.equals(hoy);
        LocalTime ahora = LocalTime.now();
        for (LocalTime h = HORA_INICIO; h.isBefore(HORA_FIN); h = h.plusMinutes(DURACION_MINUTOS)) {
            if (ocupadas.contains(h)) {
                continue;
            }
            if (esHoy && !h.isAfter(ahora)) {
                continue; // la hora ya pasó
            }
            libres.add(h);
        }
        return libres;
    }

    // Agendar cita (HU05)
    @Transactional
    public void agendar(Integer idMascota, Integer idVeterinario, Integer idServicio,
            LocalDate fecha, LocalTime hora, String motivo) {
        Mascota mascota = mascotaRepository.findById(idMascota)
                .orElseThrow(() -> new IllegalArgumentException("La mascota seleccionada no existe."));
        Veterinario veterinario = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new IllegalArgumentException("El veterinario seleccionado no existe."));
        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new IllegalArgumentException("El servicio seleccionado no existe."));

        validarHorario(idVeterinario, fecha, hora, null);

        Cita cita = new Cita();
        cita.setMascota(mascota);
        cita.setVeterinario(veterinario);
        cita.setServicio(servicio);
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setMotivo(motivo);
        cita.setEstado(EstadoCita.PROGRAMADA);
        citaRepository.save(cita);
    }

    // Cancelar cita (HU11): cambia el estado, NO elimina el registro
    @Transactional
    public void cancelar(Integer idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new IllegalArgumentException("La cita no existe: " + idCita));
        if (cita.getEstado() != EstadoCita.PROGRAMADA) {
            throw new IllegalStateException("Solo se pueden cancelar citas programadas.");
        }
        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);
    }

    // Reprogramar cita (HU12): nueva fecha, hora y, si se quiere, otro veterinario
    @Transactional
    public void reprogramar(Integer idCita, Integer idVeterinario, LocalDate fecha, LocalTime hora) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new IllegalArgumentException("La cita no existe: " + idCita));
        if (cita.getEstado() != EstadoCita.PROGRAMADA) {
            throw new IllegalStateException("Solo se pueden reprogramar citas programadas.");
        }
        Veterinario veterinario = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new IllegalArgumentException("El veterinario seleccionado no existe."));

        validarHorario(idVeterinario, fecha, hora, idCita);

        cita.setVeterinario(veterinario);
        cita.setFecha(fecha);
        cita.setHora(hora);
        citaRepository.save(cita);
    }

    // El sistema debe validar horarios disponibles antes de realizar el cambio
    private void validarHorario(Integer idVeterinario, LocalDate fecha, LocalTime hora, Integer idCitaExcluida) {
        if (!horasDisponibles(idVeterinario, fecha, idCitaExcluida).contains(hora)) {
            throw new IllegalStateException(
                    "El veterinario no tiene disponible ese horario. Elija otra fecha u hora.");
        }
    }
}