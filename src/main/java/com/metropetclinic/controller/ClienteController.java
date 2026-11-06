package com.metropetclinic.controller;

import com.metropetclinic.domain.Cliente;
import com.metropetclinic.service.ClienteService;
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
@RequestMapping("/cliente")
public class ClienteController {

    // Se enlaza un objeto ClienteService
    private final ClienteService clienteService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var clientes = clienteService.getClientes();
        model.addAttribute("clientes", clientes);
        model.addAttribute("totalClientes", clientes.size());
        return "cliente/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Cliente cliente,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.actualizado";
        if (bindingResult.hasErrors()) {
            titulo = "error";
            mensaje = "error.valores";
        } else {
            try {
                clienteService.save(cliente);
            } catch (DataIntegrityViolationException e) {
                titulo = "error";
                mensaje = "error.duplicado";
            }
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/cliente/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idCliente,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.eliminado";
        try {
            clienteService.delete(idCliente);
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
        return "redirect:/cliente/listado";
    }
}