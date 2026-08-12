package com.promptmaestro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredient")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
