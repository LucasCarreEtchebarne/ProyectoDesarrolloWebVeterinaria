package com.metropetclinic.controller;

import com.metropetclinic.domain.EstadoCita;
import com.metropetclinic.service.CitaService;
import com.metropetclinic.service.MascotaService;
import com.metropetclinic.service.ServicioService;
import com.metropetclinic.service.VeterinarioService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cita")
public class CitaController {

    // Se enlazan los servicios que ocupa la agenda
    private final CitaService citaService;
    private final MascotaService mascotaService;
    private final VeterinarioService veterinarioService;
    private final ServicioService servicioService;
    private final MessageSource messageSource;

    // Agenda del día (HU06), se puede filtrar por veterinario y fecha
    @GetMapping("/listado")
    public String listado(@RequestParam(required = false) Integer veterinarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {
        // Si no se elige fecha se muestran todas las citas
        var citas = citaService.agendaDelDia(veterinarioId, fecha);

        model.addAttribute("citas", citas);
        model.addAttribute("totalCitas", citas.size());
        model.addAttribute("programadas",
                citas.stream().filter(c -> c.getEstado() == EstadoCita.PROGRAMADA).count());
        model.addAttribute("canceladas",
                citas.stream().filter(c -> c.getEstado() == EstadoCita.CANCELADA).count());
        model.addAttribute("fecha", fecha);
        model.addAttribute("hoy", LocalDate.now());
        model.addAttribute("veterinarioId", veterinarioId);

        // Catálogos para los selects del formulario
        model.addAttribute("veterinarios", veterinarioService.getVeterinarios());
        model.addAttribute("mascotas", mascotaService.getMascotas());
        model.addAttribute("servicios", servicioService.getServicios(true));
        return "cita/listado";
    }

    // Agendar (HU05) si no trae idCita, o reprogramar (HU12) si lo trae
    @PostMapping("/guardar")
    public String guardar(@RequestParam(required = false) Integer idCita,
            @RequestParam(required = false) Integer idMascota,
            @RequestParam(required = false) Integer idServicio,
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
                    throw new IllegalArgumentException(
                            messageSource.getMessage("cita.error.faltanDatos", null, Locale.getDefault()));
                }
                citaService.agendar(idMascota, idVeterinario, idServicio, fecha, hora, motivo);
                mensaje = messageSource.getMessage("cita.agendada", new Object[]{fecha, hora}, Locale.getDefault());
            } else {
                citaService.reprogramar(idCita, idVeterinario, fecha, hora);
                mensaje = messageSource.getMessage("cita.reprogramada", new Object[]{fecha, hora}, Locale.getDefault());
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            titulo = "error";
            mensaje = e.getMessage();
        }
        redirectAttributes.addFlashAttribute(titulo, mensaje);
        return "redirect:/cita/listado";
    }

    // Cancelar cita (HU11): no se elimina, cambia a estado Cancelada
    @PostMapping("/cancelar")
    public String cancelar(@RequestParam Integer idCita,
            @RequestParam(required = false) Integer veterinarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = messageSource.getMessage("cita.cancelada", null, Locale.getDefault());
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

    // Devuelve las horas libres para llenar el select del modal (lo usa cita.js)
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