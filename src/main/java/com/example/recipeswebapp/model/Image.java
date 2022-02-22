package com.example.recipeswebapp.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne
    private Recipe recipeOwner;

    public Image(){}

    public Image(String title, Recipe recipeOwner){
        this.title=title;
        this.recipeOwner=recipeOwner;
    }
}
