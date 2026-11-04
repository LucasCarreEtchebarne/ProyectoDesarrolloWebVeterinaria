package metropet_clinic.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mascotaId; 
    private LocalDate fechaEmision;
    private Double total;

    @Column(columnDefinition = "TEXT")
    private String detalleServicios; // Se pone la describcion de los sevicios cobrados

    // Se crea los constructores
    public Factura() {
    }

    public Factura(Long mascotaId, LocalDate fechaEmision, Double total, String detalleServicios) {
        this.mascotaId = mascotaId;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.detalleServicios = detalleServicios;
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

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDetalleServicios() {
        return detalleServicios;
    }

    public void setDetalleServicios(String detalleServicios) {
        this.detalleServicios = detalleServicios;
    }
}