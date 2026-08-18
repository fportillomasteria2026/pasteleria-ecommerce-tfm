package com.promptmaestro.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarta")
public class Tarta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificacion
    @Column(length = 50)
    private String sku;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    // Caracteristicas de la tarta
    @Column(nullable = false, length = 10)
    private String tamano; // XS, S, M, L, XL

    @Column(nullable = false)
    private Integer pisos; // 2 o 3

    @Column(nullable = false, length = 20)
    private String forma; // Cilindrica, Cuadrada, Rectangular

    @Column(length = 50)
    private String dimensiones; // Ej: "30x30cm", "25cm diametro"

    // Sabores y rellenos
    @Column(name = "sabor_bizcocho", length = 50)
    private String saborBizcocho; // Chocolate, Vainilla, Red Velvet, etc.

    @Column(length = 200)
    private String frutas; // Fresa, Arandano, etc. separadas por coma

    @Column(name = "tipo_crema", length = 50)
    private String tipoCrema; // Buttercream, Ganache, Crema Chantilly, etc.

    // Personalizacion
    @Column(name = "tipo_personalizacion", length = 50)
    private String tipoPersonalizacion; // Papeleria, Papel de Azucar, Mezcla

    // Precios
    @Column(name = "precio_publico", nullable = false)
    private Double precioPublico;

    @Column
    private Double coste;

    // Estado
    @Column(nullable = false)
    private Boolean disponible = true;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(columnDefinition = "TEXT")
    private String notas;

    // Auditoria
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Tarta() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }
    public Integer getPisos() { return pisos; }
    public void setPisos(Integer pisos) { this.pisos = pisos; }
    public String getForma() { return forma; }
    public void setForma(String forma) { this.forma = forma; }
    public String getDimensiones() { return dimensiones; }
    public void setDimensiones(String dimensiones) { this.dimensiones = dimensiones; }
    public String getSaborBizcocho() { return saborBizcocho; }
    public void setSaborBizcocho(String saborBizcocho) { this.saborBizcocho = saborBizcocho; }
    public String getFrutas() { return frutas; }
    public void setFrutas(String frutas) { this.frutas = frutas; }
    public String getTipoCrema() { return tipoCrema; }
    public void setTipoCrema(String tipoCrema) { this.tipoCrema = tipoCrema; }
    public String getTipoPersonalizacion() { return tipoPersonalizacion; }
    public void setTipoPersonalizacion(String tipoPersonalizacion) { this.tipoPersonalizacion = tipoPersonalizacion; }
    public Double getPrecioPublico() { return precioPublico; }
    public void setPrecioPublico(Double precioPublico) { this.precioPublico = precioPublico; }
    public Double getCoste() { return coste; }
    public void setCoste(Double coste) { this.coste = coste; }
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
