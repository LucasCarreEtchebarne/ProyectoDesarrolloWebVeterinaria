package metropet_clinic.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import metropet_clinic.demo.model.Cita;
import metropet_clinic.demo.model.EstadoCita;
import metropet_clinic.demo.repository.MascotaRepository;
import metropet_clinic.demo.repository.VeterinarioRepository;
import metropet_clinic.demo.service.CitaService;
import metropet_clinic.demo.service.ServicioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cita")
public class CitaController {

    private final CitaService citaService;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final ServicioService servicioService;

    public CitaController(CitaService citaService,
            MascotaRepository mascotaRepository,
            VeterinarioRepository veterinarioRepository,
            ServicioService servicioService) {
        this.citaService = citaService;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.servicioService = servicioService;
    }

    @GetMapping("/listado")
    public String listado(@RequestParam(required = false) Integer veterinarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {
        LocalDate dia = (fecha != null) ? fecha : LocalDate.now();
        List<Cita> citas = citaService.agendaDelDia(veterinarioId, dia);

        model.addAttribute("citas", citas);
        model.addAttribute("totalCitas", citas.size());
        model.addAttribute("programadas",
                citas.stream().filter(c -> c.getEstado() == EstadoCita.PROGRAMADA).count());
        model.addAttribute("canceladas",
                citas.stream().filter(c -> c.getEstado() == EstadoCita.CANCELADA).count());
        model.addAttribute("fecha", dia);
        model.addAttribute("hoy", LocalDate.now());
        model.addAttribute("veterinarioId", veterinarioId);

        // Catálogos para los selects del formulario
        model.addAttribute("veterinarios", veterinarioRepository.findAll());
        model.addAttribute("mascotas", mascotaRepository.findAll());
        model.addAttribute("servicios", servicioService.listarServicios());
        return "cita/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam(required = false) Integer idCita,
            @RequestParam(required = false) Long idMascota,
            @RequestParam(required = false) Long idServicio,
            @RequestParam Integer idVeterinario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
            @RequestParam(required = false) String motivo,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String mensaje;
        try {
            if (idCita == null) {
                if (idMascota == null || idServicio == null) {
                    throw new IllegalArgumentException("Debe seleccionar la mascota y el servicio.");
                }
                citaService.agendar(idMascota, idVeterinario, idServicio, fecha, hora, motivo);
                mensaje = "Cita agendada para el " + fecha + " a las " + hora + ".";
            } else {
                citaService.reprogramar(idCita, idVeterinario, fecha, hora);
                mensaje = "Cita reprogramada para el " + fecha + " a las " + hora + ".";
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            titulo = "error";
            mensaje = e.getMessage();
        }
        redirectAttributes.addFlashAttribute(titulo, mensaje);
        // Se vuelve a la agenda del veterinario y la fecha elegidos
        redirectAttributes.addAttribute("veterinarioId", idVeterinario);
        redirectAttributes.addAttribute("fecha", fecha.toString());
        return "redirect:/cita/listado";
    }

    @PostMapping("/cancelar")
    public String cancelar(@RequestParam Integer idCita,
            @RequestParam(required = false) Integer veterinarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String mensaje = "La cita fue cancelada.";
        try {
            citaService.cancelar(idCita);
        } catch (IllegalArgumentException | IllegalStateException e) {
            titulo = "error";
            mensaje = e.getMessage();
        }
        redirectAttributes.addFlashAttribute(titulo, mensaje);
        if (veterinarioId != null) {
            redirectAttributes.addAttribute("veterinarioId", veterinarioId);
        }
        if (fecha != null) {
            redirectAttributes.addAttribute("fecha", fecha.toString());
        }
        return "redirect:/cita/listado";
    }

    @GetMapping("/disponibilidad")
    @ResponseBody
    public List<String> disponibilidad(@RequestParam Integer idVeterinario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Integer idCita) {
        return citaService.horasDisponibles(idVeterinario, fecha, idCita)
                .stream()
                .map(LocalTime::toString)
                .toList();
    }
}
