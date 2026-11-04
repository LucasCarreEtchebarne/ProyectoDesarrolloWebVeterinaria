package metropet_clinic.demo.controller;

import metropet_clinic.demo.model.ExpedienteMedico;
import metropet_clinic.demo.model.Vacuna;
import metropet_clinic.demo.service.ExpedienteMedicoService;
import metropet_clinic.demo.service.VacunaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/veterinario")
public class VeterinarioController {

    @Autowired
    private ExpedienteMedicoService expedienteService;

    @Autowired
    private VacunaService vacunaService;

    // La vista para mostrar el formulario de registro clínico 
    @GetMapping("/atender/{mascotaId}")
    public String mostrarFormularioAtencion(@PathVariable Long mascotaId, Model model) {
        ExpedienteMedico expediente = new ExpedienteMedico();
        expediente.setMascotaId(mascotaId);
        expediente.setFecha(LocalDate.now());

        Vacuna vacuna = new Vacuna();
        vacuna.setMascotaId(mascotaId);
        vacuna.setFechaAplicacion(LocalDate.now());

        model.addAttribute("expediente", expediente);
        model.addAttribute("vacuna", vacuna);
        model.addAttribute("mascotaId", mascotaId);

        return "form-atencion"; // html
    }

    // Se guarda el diagnóstico 
    @PostMapping("/guardar-expediente")
    public String guardarExpediente(@ModelAttribute ExpedienteMedico expediente) {
        expediente.setFecha(LocalDate.now());
        expedienteService.guardarExpediente(expediente);
        return "redirect:/veterinario/atender/" + expediente.getMascotaId() + "?exitoExpediente";
    }
  // Se guarda la vacuna 
    @PostMapping("/guardar-vacuna")
    public String guardarVacuna(@ModelAttribute Vacuna vacuna) {
        vacuna.setFechaAplicacion(LocalDate.now());
        vacunaService.guardarVacuna(vacuna);
        return "redirect:/veterinario/atender/" + vacuna.getMascotaId() + "?exitoVacuna";
    }
}