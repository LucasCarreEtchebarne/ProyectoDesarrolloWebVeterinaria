package com.metropetclinic.controller;

import com.metropetclinic.domain.Servicio;
import com.metropetclinic.service.ServicioService;
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
@RequestMapping("/servicio")
public class ServicioController {

    // Se enlaza un objeto ServicioService
    private final ServicioService servicioService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var servicios = servicioService.getServicios(false);
        model.addAttribute("servicios", servicios);
        model.addAttribute("totalServicios", servicios.size());
        return "servicio/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Servicio servicio,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.actualizado";
        if (bindingResult.hasErrors()) {
            titulo = "error";
            mensaje = "error.valores";
        } else {
            try {
                servicioService.save(servicio);
            } catch (DataIntegrityViolationException e) {
                titulo = "error";
                mensaje = "error.duplicado";
            }
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/servicio/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idServicio,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.eliminado";
        try {
            servicioService.delete(idServicio);
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
        return "redirect:/servicio/listado";
    }
}