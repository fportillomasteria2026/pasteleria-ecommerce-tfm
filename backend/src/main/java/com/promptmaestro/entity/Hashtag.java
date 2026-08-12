package com.promptmaestro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hashtag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
