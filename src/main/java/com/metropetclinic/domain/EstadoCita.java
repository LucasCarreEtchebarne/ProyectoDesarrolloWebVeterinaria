package com.metropetclinic.domain;

public enum EstadoCita {
    PROGRAMADA("Programada", "primary"),
    CANCELADA("Cancelada", "danger"),
    COMPLETADA("Completada", "success");

    private final String etiqueta;
    private final String color; // color de Bootstrap para el badge

    EstadoCita(String etiqueta, String color) {
        this.etiqueta = etiqueta;
        this.color = color;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getColor() {
        return color;
    }
}