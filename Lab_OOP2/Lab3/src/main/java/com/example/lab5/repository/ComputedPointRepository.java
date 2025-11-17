package com.example.lab5.repository;

import com.example.lab5.entity.ComputedPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComputedPointRepository extends JpaRepository<ComputedPoint, Long> {
    List<ComputedPoint> findByFunctionIdOrderByX(Long functionId);
}