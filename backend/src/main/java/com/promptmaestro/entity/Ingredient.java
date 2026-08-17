package com.promptmaestro.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double stockQuantity;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private String category;

    public Ingredient() {}

    public Ingredient(Long id, String name, Double stockQuantity, String unit, String category) {
        this.id = id;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.category = category;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Double stockQuantity) { this.stockQuantity = stockQuantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private Double stockQuantity;
        private String unit;
        private String category;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder stockQuantity(Double stockQuantity) { this.stockQuantity = stockQuantity; return this; }
        public Builder unit(String unit) { this.unit = unit; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Ingredient build() { return new Ingredient(id, name, stockQuantity, unit, category); }
    }
}
