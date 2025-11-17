package com.example.lab5.service;

import com.example.lab5.dto.FunctionResponse;
import com.example.lab5.entity.FunctionEntity;
import com.example.lab5.repository.FunctionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionService {
    private final FunctionRepository repo;
    private final Logger log = LoggerFactory.getLogger(FunctionService.class);

    public FunctionService(FunctionRepository repo){ this.repo = repo; }

    public FunctionResponse toDto(FunctionEntity e){
        FunctionResponse r = new FunctionResponse();
        r.setId(e.getId());
        r.setUserId(e.getUserId());
        r.setName(e.getName());
        r.setExpression(e.getExpression());
        r.setDescription(e.getDescription());
        return r;
    }

    public List<FunctionResponse> listByUser(Long userId){
        List<FunctionEntity> list = repo.findByUserId(userId);
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }
}