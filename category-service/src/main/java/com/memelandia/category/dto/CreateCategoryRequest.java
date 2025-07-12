package com.memelandia.category.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateCategoryRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    private String name;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String description;
    
    public CreateCategoryRequest() {}
    
    public CreateCategoryRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}