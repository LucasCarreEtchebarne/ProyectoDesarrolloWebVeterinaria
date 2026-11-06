package com.metropetclinic.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "servicio")
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer idServicio;

    @Column(unique = true, nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String nombreServicio;

    @Column(length = 255)
    @Size(max = 255)
    private String descripcion;

    @Column(precision = 12, scale = 2)
    @NotNull(message = "El precio no puede ser vacio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El precio debe ser mayor o igual a cero")
    private BigDecimal precio;

    @Column(nullable = false)
    private boolean estado;
}