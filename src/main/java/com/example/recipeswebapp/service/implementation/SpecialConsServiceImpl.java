package com.example.recipeswebapp.service.implementation;


import com.example.recipeswebapp.model.SpecialConsideration;
import com.example.recipeswebapp.repository.SpecialConsiderationRepository;
import com.example.recipeswebapp.service.interfaces.SpecialConsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialConsServiceImpl implements SpecialConsService {

    private final SpecialConsiderationRepository specialConsiderationRepository;

    public SpecialConsServiceImpl(SpecialConsiderationRepository specialConsiderationRepository) {
        this.specialConsiderationRepository = specialConsiderationRepository;
    }

    @Override
    public List<SpecialConsideration> getAllSpecConsiderations() {
        return specialConsiderationRepository.findAll();
    }
}
