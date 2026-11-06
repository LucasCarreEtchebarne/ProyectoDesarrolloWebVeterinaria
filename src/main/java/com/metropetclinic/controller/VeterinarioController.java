package com.metropetclinic.controller;

import com.metropetclinic.domain.Veterinario;
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
@RequestMapping("/veterinario")
public class VeterinarioController {

    // Se enlaza un objeto VeterinarioService
    private final VeterinarioService veterinarioService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var veterinarios = veterinarioService.getVeterinarios();
        model.addAttribute("veterinarios", veterinarios);
        model.addAttribute("totalVeterinarios", veterinarios.size());
        return "veterinario/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Veterinario veterinario,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.actualizado";
        if (bindingResult.hasErrors()) {
            titulo = "error";
            mensaje = "error.valores";
        } else {
            try {
                veterinarioService.save(veterinario);
            } catch (DataIntegrityViolationException e) {
                titulo = "error";
                mensaje = "error.duplicado";
            }
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/veterinario/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idVeterinario,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.eliminado";
        try {
            veterinarioService.delete(idVeterinario);
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
        return "redirect:/veterinario/listado";
    }
}