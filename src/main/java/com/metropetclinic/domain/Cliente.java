package com.metropetclinic.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank
    @Size(max = 20)
    private String identificacion;

    @Column(nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String nombre;

    @Column(length = 20)
    @Size(max = 20)
    private String telefono;

    @Column(length = 100)
    @Size(max = 100)
    private String correo;

    @Column(length = 255)
    @Size(max = 255)
    private String direccion;
}