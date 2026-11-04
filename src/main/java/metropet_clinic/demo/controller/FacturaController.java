package metropet_clinic.demo.controller;

import metropet_clinic.demo.service.ServicioService;
import metropet_clinic.demo.model.Factura;
import metropet_clinic.demo.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/facturacion")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;
    @Autowired
    private ServicioService servicioService;

    // Mostrar el formulario de compra y facturación
    @GetMapping("/nuevo/{mascotaId}")
    public String mostrarFormularioFactura(@PathVariable Long mascotaId, Model model) {
        Factura factura = new Factura();
        factura.setMascotaId(mascotaId);
        factura.setFechaEmision(LocalDate.now());
        factura.setTotal(10000.0);
        factura.setDetalleServicios("Consulta general y aplicación de vacuna");

        model.addAttribute("factura", factura);
        return "factura-form"; 
    } 

    @PostMapping("/guardar")
    public String guardarFactura(@ModelAttribute Factura factura) {
        factura.setFechaEmision(LocalDate.now());
        facturaService.guardarFactura(factura);
        return "redirect:/facturacion/catalogo?exito=true";
    }
    
    //Si se toca la opcion de catalogo se muestra 
    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model) {
        model.addAttribute("servicios", servicioService.listarServicios());
        return "lista-servicios";
}
}