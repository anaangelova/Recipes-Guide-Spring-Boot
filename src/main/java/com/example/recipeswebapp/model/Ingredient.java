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

    public Ingredient(String name,Double quantity, Measurement measurement){
        this.name=name;
        this.quantity=quantity;
        this.measurement=measurement;
    }

    public String getFormatted(){
        return String.format("%.0f %s %s",this.quantity,this.measurement.name(),this.name);
    }

}
