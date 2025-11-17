package com.example.lab5.repository;

import com.example.lab5.entity.FunctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FunctionRepository extends JpaRepository<FunctionEntity, Long> {
    List<FunctionEntity> findByUserId(Long userId);
}