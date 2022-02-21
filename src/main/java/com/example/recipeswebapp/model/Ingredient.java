package com.example.recipeswebapp.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double quantity;

    @Enumerated(EnumType.STRING)
    private Measurement measurement;

    public Ingredient(){}

}
