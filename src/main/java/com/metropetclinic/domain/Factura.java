package com.metropetclinic.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "factura")
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Integer idFactura;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank
    @Size(max = 20)
    private String numeroFactura;

    @Column(nullable = false)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaEmision;

    @Column(precision = 12, scale = 2)
    @NotNull(message = "El monto no puede ser vacio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El monto debe ser mayor o igual a cero")
    private BigDecimal montoTotal;

    @Column(nullable = false, length = 30)
    @NotBlank
    @Size(max = 30)
    private String metodoPago;

    // Cliente al que se le cobra (HU15)
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    // Cita que se está facturando
    @ManyToOne
    @JoinColumn(name = "id_cita")
    private Cita cita;
}