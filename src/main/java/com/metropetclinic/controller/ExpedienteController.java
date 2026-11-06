package com.metropetclinic.controller;

import com.metropetclinic.domain.ExpedienteMedico;
import com.metropetclinic.service.ExpedienteMedicoService;
import com.metropetclinic.service.MascotaService;
import com.metropetclinic.service.VeterinarioService;
import jakarta.validation.Valid;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/expediente")
public class ExpedienteController {

    // Se enlaza un objeto ExpedienteMedicoService
    private final ExpedienteMedicoService expedienteMedicoService;
    private final MascotaService mascotaService;
    private final VeterinarioService veterinarioService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var expedientes = expedienteMedicoService.getExpedientes();
        model.addAttribute("expedientes", expedientes);
        model.addAttribute("totalExpedientes", expedientes.size());
        model.addAttribute("mascotas", mascotaService.getMascotas());
        model.addAttribute("veterinarios", veterinarioService.getVeterinarios());
        return "expediente/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid ExpedienteMedico expedienteMedico,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.actualizado";
        if (bindingResult.hasErrors()) {
            titulo = "error";
            mensaje = "error.valores";
        } else {
            try {
                expedienteMedicoService.save(expedienteMedico);
            } catch (DataIntegrityViolationException e) {
                titulo = "error";
                mensaje = "error.duplicado";
            }
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/expediente/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idExpediente,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.eliminado";
        try {
            expedienteMedicoService.delete(idExpediente);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            mensaje = "error.noExiste";
        } catch (IllegalStateException e) {
            titulo = "error";
            mensaje = "error.datosAsociados";
        } catch (Exception e) {
            titulo = "error";
            mensaje = "error.inesperado";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/expediente/listado";
    }
}