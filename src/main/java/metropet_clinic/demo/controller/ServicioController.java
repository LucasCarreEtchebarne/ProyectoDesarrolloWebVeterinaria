package metropet_clinic.demo.controller;

import metropet_clinic.demo.model.Servicio;
import metropet_clinic.demo.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioService.listarServicios());
        return "lista-servicios"; 
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("servicio", new Servicio());
        return "form-servicio"; 
    }

    @PostMapping("/guardar")
    public String guardarServicio(@ModelAttribute("servicio") Servicio servicio) {
        servicioService.guardarServicio(servicio);
        return "redirect:/servicios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Servicio servicio = servicioService.obtenerServicioPorId(id);
        model.addAttribute("servicio", servicio);
        return "form-servicio"; 
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable("id") Long id) {
        servicioService.eliminarServicio(id);
        return "redirect:/servicios";
    }
}