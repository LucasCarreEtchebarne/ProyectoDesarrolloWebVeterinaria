package com.metropetclinic.controller;

import com.metropetclinic.domain.Factura;
import com.metropetclinic.service.CitaService;
import com.metropetclinic.service.ClienteService;
import com.metropetclinic.service.FacturaService;
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
@RequestMapping("/factura")
public class FacturaController {

    // Se enlaza un objeto FacturaService
    private final FacturaService facturaService;
    private final ClienteService clienteService;
    private final CitaService citaService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var facturas = facturaService.getFacturas();
        model.addAttribute("facturas", facturas);
        model.addAttribute("totalFacturas", facturas.size());
        model.addAttribute("clientes", clienteService.getClientes());
        model.addAttribute("citas", citaService.getCitas());
        return "factura/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Factura factura,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.actualizado";
        if (bindingResult.hasErrors()) {
            titulo = "error";
            mensaje = "error.valores";
        } else {
            try {
                facturaService.save(factura);
            } catch (DataIntegrityViolationException e) {
                titulo = "error";
                mensaje = "error.duplicado";
            }
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/factura/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idFactura,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String mensaje = "mensaje.eliminado";
        try {
            facturaService.delete(idFactura);
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
        return "redirect:/factura/listado";
    }
}