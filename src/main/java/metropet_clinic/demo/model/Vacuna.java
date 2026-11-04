package metropet_clinic.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacunas")
public class Vacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mascotaId;    // Se pide el id de la mascota para agregar cual vacuna se le coloco
    private String nombreVacuna;
    private LocalDate fechaAplicacion;
    private LocalDate fechaProximaDosis;

    // Se crean los constructores
    public Vacuna() {
    }

    public Vacuna(Long mascotaId, String nombreVacuna, LocalDate fechaAplicacion, LocalDate fechaProximaDosis) {
        this.mascotaId = mascotaId;
        this.nombreVacuna = nombreVacuna;
        this.fechaAplicacion = fechaAplicacion;
        this.fechaProximaDosis = fechaProximaDosis;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
    }

    public String getNombreVacuna() {
        return nombreVacuna;
    }

    public void setNombreVacuna(String nombreVacuna) {
        this.nombreVacuna = nombreVacuna;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(LocalDate fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public LocalDate getFechaProximaDosis() {
        return fechaProximaDosis;
    }

    public void setFechaProximaDosis(LocalDate fechaProximaDosis) {
        this.fechaProximaDosis = fechaProximaDosis;
    }
}
