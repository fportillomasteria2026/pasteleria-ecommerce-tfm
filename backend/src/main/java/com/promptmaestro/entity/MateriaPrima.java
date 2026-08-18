package com.promptmaestro.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "materia_prima")
public class MateriaPrima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_sku", length = 50)
    private String codigoSku;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String marca;

    @Column(length = 100)
    private String proveedor;

    private Double coste;

    @Column(length = 50)
    private String formato;

    private Double peso;

    @Column(length = 20)
    private String unidad;

    private Double cantidad;

    public MateriaPrima() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigoSku() { return codigoSku; }
    public void setCodigoSku(String codigoSku) { this.codigoSku = codigoSku; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }
    public Double getCoste() { return coste; }
    public void setCoste(Double coste) { this.coste = coste; }
    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { this.cantidad = cantidad; }
}
