package com.metropetclinic.controller;

import com.metropetclinic.domain.Mascota;
import com.metropetclinic.service.ClienteService;
import com.metropetclinic.service.MascotaService;
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
@RequestMapping("/mascota")
public class MascotaController {

    // Se enlaza un objeto MascotaService
    private final MascotaService mascotaService;
    private final ClienteService clienteService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var mascotas = mascotaService.getMascotas();
        model.addAttribute("mascotas", mascotas);
        model.addAttribute("totalMascotas", mascotas.size());
        model.addAttribute("clientes", clienteService.getClientes());
        return "mascota/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Mascota mascota,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.actualizado";
        if (bindingResult.hasErrors()) {
            titulo = "error";
            mensaje = "error.valores";
        } else {
            try {
                mascotaService.save(mascota);
            } catch (DataIntegrityViolationException e) {
                titulo = "error";
                mensaje = "error.duplicado";
            }
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/mascota/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idMascota,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.eliminado";
        try {
            mascotaService.delete(idMascota);
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
        return "redirect:/mascota/listado";
    }
}