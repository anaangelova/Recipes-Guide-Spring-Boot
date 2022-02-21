package com.example.recipeswebapp.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @ManyToOne
    private Recipe recipe;

    public Instruction(){}
}
