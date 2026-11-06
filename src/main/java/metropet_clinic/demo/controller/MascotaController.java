package metropet_clinic.demo.controller;

import metropet_clinic.demo.model.Mascota;
import metropet_clinic.demo.model.Cliente;
import metropet_clinic.demo.service.MascotaService;
import metropet_clinic.demo.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarMascotas(Model model) {

        model.addAttribute("mascotas",
                mascotaService.listarMascotas());

        return "mascotas/lista";
    }

    @GetMapping("/nuevo")
    public String nuevaMascota(Model model) {

        model.addAttribute("mascota", new Mascota());

        model.addAttribute("clientes",
                clienteService.listarClientes());

        return "mascotas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarMascota(
            @PathVariable Long id,
            Model model) {

        Mascota mascota = mascotaService.obtenerMascota(id);

        if (mascota == null) {
            return "redirect:/mascotas";
        }

        model.addAttribute("mascota", mascota);

        model.addAttribute("clientes",
                clienteService.listarClientes());

        return "mascotas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMascota(
            @ModelAttribute Mascota mascota,
            @RequestParam("idCliente") Long idCliente) {

        Cliente cliente = clienteService.obtenerCliente(idCliente);

        if (cliente == null) {
            return "redirect:/mascotas/nuevo";
        }

        mascota.setCliente(cliente);

        mascotaService.guardarMascota(mascota);

        return "redirect:/mascotas";
    }
}