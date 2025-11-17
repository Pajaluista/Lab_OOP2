package com.example.lab5.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "functions")
public class FunctionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String name;

    @Column(columnDefinition = "text")
    private String expression;

    private String description;

    private Instant createdAt = Instant.now();

    // getters/setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public Long getUserId(){ return userId; }
    public void setUserId(Long userId){ this.userId = userId; }
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
    public String getExpression(){ return expression; }
    public void setExpression(String expression){ this.expression = expression; }
    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }
    public Instant getCreatedAt(){ return createdAt; }
    public void setCreatedAt(Instant createdAt){ this.createdAt = createdAt; }
}