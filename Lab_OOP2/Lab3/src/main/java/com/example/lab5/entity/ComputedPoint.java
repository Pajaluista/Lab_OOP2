package com.example.lab5.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "computed_points")
public class ComputedPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "function_id", nullable = false)
    private Long functionId;

    private Double x;
    private Double y;
    private Instant computedAt = Instant.now();

    // getters/setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public Long getFunctionId(){ return functionId; }
    public void setFunctionId(Long functionId){ this.functionId = functionId; }
    public Double getX(){ return x; }
    public void setX(Double x){ this.x = x; }
    public Double getY(){ return y; }
    public void setY(Double y){ this.y = y; }
    public Instant getComputedAt(){ return computedAt; }
    public void setComputedAt(Instant computedAt){ this.computedAt = computedAt; }
}