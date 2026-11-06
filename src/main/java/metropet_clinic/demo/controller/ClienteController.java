package metropet_clinic.demo.controller;

import metropet_clinic.demo.model.Cliente;
import metropet_clinic.demo.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes",
                clienteService.listarClientes());

        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());

        return "clientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {

        Cliente cliente = clienteService.obtenerCliente(id);

        if (cliente == null) {
            return "redirect:/clientes";
        }

        model.addAttribute("cliente", cliente);

        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCliente(
            @ModelAttribute Cliente cliente) {

        clienteService.guardarCliente(cliente);

        return "redirect:/clientes";
    }
}
