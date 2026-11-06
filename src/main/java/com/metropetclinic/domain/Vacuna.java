package com.metropetclinic.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "vacuna")
public class Vacuna implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacuna")
    private Integer idVacuna;

    @Column(nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String nombreVacuna;

    @Column(nullable = false)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaAplicacion;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate proximaDosis;

    // Cada vacuna queda registrada en un expediente médico (HU09)
    @ManyToOne
    @JoinColumn(name = "id_expediente")
    private ExpedienteMedico expediente;
}