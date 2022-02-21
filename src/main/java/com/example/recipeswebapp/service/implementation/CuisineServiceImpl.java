package com.example.recipeswebapp.service.implementation;

import com.example.recipeswebapp.model.Cuisine;
import com.example.recipeswebapp.repository.CuisineRepository;
import com.example.recipeswebapp.service.interfaces.CuisineService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuisineServiceImpl implements CuisineService {

    private final CuisineRepository cuisineRepository;

    public CuisineServiceImpl(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    @Override
    public List<Cuisine> getAllCuisines() {
        return cuisineRepository.findAll();
    }
}
