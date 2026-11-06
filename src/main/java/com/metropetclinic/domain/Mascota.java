package com.metropetclinic.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "mascota")
public class Mascota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota")
    private Integer idMascota;

    @Column(nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private String especie;

    @Column(length = 50)
    @Size(max = 50)
    private String raza;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    // Cada mascota pertenece a un cliente (HU03)
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
}