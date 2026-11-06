package com.metropetclinic.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cita")
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Column(columnDefinition = "TEXT")
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cita", nullable = false, length = 20)
    private EstadoCita estado = EstadoCita.PROGRAMADA;

    // Una mascota puede tener muchas citas
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_mascota")
    private Mascota mascota;

    // Un veterinario atiende muchas citas
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_veterinario")
    private Veterinario veterinario;

    // Cada cita es para un servicio del catálogo
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;
}