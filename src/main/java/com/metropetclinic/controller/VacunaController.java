package com.metropetclinic.controller;

import com.metropetclinic.domain.Vacuna;
import com.metropetclinic.service.ExpedienteMedicoService;
import com.metropetclinic.service.VacunaService;
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
@RequestMapping("/vacuna")
public class VacunaController {

    // Se enlaza un objeto VacunaService
    private final VacunaService vacunaService;
    private final ExpedienteMedicoService expedienteMedicoService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var vacunas = vacunaService.getVacunas();
        model.addAttribute("vacunas", vacunas);
        model.addAttribute("totalVacunas", vacunas.size());
        model.addAttribute("expedientes", expedienteMedicoService.getExpedientes());
        return "vacuna/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Vacuna vacuna,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.actualizado";
        if (bindingResult.hasErrors()) {
            titulo = "error";
            mensaje = "error.valores";
        } else {
            try {
                vacunaService.save(vacuna);
            } catch (DataIntegrityViolationException e) {
                titulo = "error";
                mensaje = "error.duplicado";
            }
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/vacuna/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idVacuna,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.eliminado";
        try {
            vacunaService.delete(idVacuna);
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
        return "redirect:/vacuna/listado";
    }
}