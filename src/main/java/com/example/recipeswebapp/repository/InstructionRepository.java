package com.example.recipeswebapp.repository;

import com.example.recipeswebapp.model.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructionRepository  extends JpaRepository<Instruction,Long> {
}
