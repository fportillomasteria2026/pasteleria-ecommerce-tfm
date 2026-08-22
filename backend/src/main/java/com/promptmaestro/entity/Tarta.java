package com.promptmaestro.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarta")
public class Tarta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String sku;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    @Column(columnDefinition = "TEXT")
    private String hashtags;

    @Column(nullable = false, length = 10)
    private String tamano;

    @Column(nullable = false)
    private Integer pisos;

    @Column(nullable = false, length = 20)
    private String forma;

    @Column(length = 50)
    private String dimensiones;

    @Column(name = "sabor_bizcocho", length = 50)
    private String saborBizcocho;

    @Column(length = 200)
    private String frutas;

    @Column(name = "tipo_crema", length = 50)
    private String tipoCrema;

    @Column(name = "tipo_personalizacion", length = 50)
    private String tipoPersonalizacion;

    @Column(name = "precio_publico", nullable = false)
    private Double precioPublico;

    @Column
    private Double coste;

    @Column(nullable = false)
    private Boolean disponible = true;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Tarta() {}

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
    public String getHashtags() { return hashtags; }
    public void setHashtags(String hashtags) { this.hashtags = hashtags; }
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
