package metropet_clinic.demo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import metropet_clinic.demo.model.Cita;
import metropet_clinic.demo.model.EstadoCita;
import metropet_clinic.demo.model.Mascota;
import metropet_clinic.demo.model.Servicio;
import metropet_clinic.demo.model.Veterinario;
import metropet_clinic.demo.repository.CitaRepository;
import metropet_clinic.demo.repository.MascotaRepository;
import metropet_clinic.demo.repository.ServicioRepository;
import metropet_clinic.demo.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaService {

    // Horario de atención: citas de 30 min entre las 8:00 y las 17:00
    public static final LocalTime HORA_INICIO = LocalTime.of(8, 0);
    public static final LocalTime HORA_FIN = LocalTime.of(17, 0);
    public static final int DURACION_MINUTOS = 30;

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final ServicioRepository servicioRepository;

    public CitaService(CitaRepository citaRepository,
            MascotaRepository mascotaRepository,
            VeterinarioRepository veterinarioRepository,
            ServicioRepository servicioRepository) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.servicioRepository = servicioRepository;
    }

    // Agenda del día. Si idVeterinario es null se muestran todos los veterinarios.
    @Transactional(readOnly = true)
    public List<Cita> agendaDelDia(Integer idVeterinario, LocalDate fecha) {
        if (idVeterinario == null) {
            return citaRepository.findByFechaOrderByHoraAsc(fecha);
        }
        return citaRepository.findByVeterinarioIdVeterinarioAndFechaOrderByHoraAsc(idVeterinario, fecha);
    }

    @Transactional(readOnly = true)
    public Cita getCita(Integer idCita) {
        return citaRepository.findById(idCita)
                .orElseThrow(() -> new IllegalArgumentException("La cita no existe: " + idCita));
    }

    
    //Horas libres de un veterinario en una fecha.
    //idCitaExcluida sirve al reprogramar: la hora que ya ocupa esa cita cuenta como libre.
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

    //Agendar cita
    @Transactional
    public Cita agendar(Long idMascota, Integer idVeterinario, Long idServicio,
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
        return citaRepository.save(cita);
    }

    //Cancelar cita (cambia el estado, NO elimina el registro)
    @Transactional
    public Cita cancelar(Integer idCita) {
        Cita cita = getCita(idCita);
        if (cita.getEstado() != EstadoCita.PROGRAMADA) {
            throw new IllegalStateException("Solo se pueden cancelar citas programadas.");
        }
        cita.setEstado(EstadoCita.CANCELADA);
        return citaRepository.save(cita);
    }

    //Reprogramar cita (nueva fecha, hora y, si se quiere, otro veterinario)
    @Transactional
    public Cita reprogramar(Integer idCita, Integer idVeterinario, LocalDate fecha, LocalTime hora) {
        Cita cita = getCita(idCita);
        if (cita.getEstado() != EstadoCita.PROGRAMADA) {
            throw new IllegalStateException("Solo se pueden reprogramar citas programadas.");
        }
        Veterinario veterinario = veterinarioRepository.findById(idVeterinario)
                .orElseThrow(() -> new IllegalArgumentException("El veterinario seleccionado no existe."));

        validarHorario(idVeterinario, fecha, hora, idCita);

        cita.setVeterinario(veterinario);
        cita.setFecha(fecha);
        cita.setHora(hora);
        return citaRepository.save(cita);
    }

    //El sistema debe validar horarios disponibles antes de realizar el cambio
    private void validarHorario(Integer idVeterinario, LocalDate fecha, LocalTime hora, Integer idCitaExcluida) {
        if (!horasDisponibles(idVeterinario, fecha, idCitaExcluida).contains(hora)) {
            throw new IllegalStateException(
                    "El veterinario no tiene disponible ese horario. Elija otra fecha u hora.");
        }
    }
}