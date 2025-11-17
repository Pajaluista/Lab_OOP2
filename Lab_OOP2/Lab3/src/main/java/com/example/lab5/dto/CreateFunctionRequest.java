package com.example.lab5.dto;

public class CreateFunctionRequest {
    private Long userId;
    private String name;
    private String expression;
    private String description;

    public Long getUserId(){ return userId; }
    public void setUserId(Long userId){ this.userId = userId; }
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
    public String getExpression(){ return expression; }
    public void setExpression(String expression){ this.expression = expression; }
    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }
}