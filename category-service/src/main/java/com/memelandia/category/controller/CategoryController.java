package com.memelandia.category.controller;

import com.memelandia.category.dto.CategoryDTO;
import com.memelandia.category.dto.CreateCategoryRequest;
import com.memelandia.category.service.CategoryService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    
    @Autowired
    private CategoryService categoryService;
    
    private final Counter categoryCreatedCounter;
    private final Counter categoryAccessCounter;
    
    public CategoryController(MeterRegistry meterRegistry) {
        this.categoryCreatedCounter = Counter.builder("categories_created_total")
                .description("Total de categorias criadas")
                .register(meterRegistry);
        
        this.categoryAccessCounter = Counter.builder("categories_accessed_total")
                .description("Total de acessos às categorias")
                .register(meterRegistry);
    }
    
    @GetMapping
    @Timed(value = "category.find.all", description = "Tempo para buscar todas as categorias")
    public ResponseEntity<List<CategoryDTO>> findAll() {
        logger.info("Recebida requisição para buscar todas as categorias");
        categoryAccessCounter.increment();
        
        List<CategoryDTO> categories = categoryService.findAll();
        logger.info("Retornando {} categorias", categories.size());
        
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{id}")
    @Timed(value = "category.find.by.id", description = "Tempo para buscar categoria por ID")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        logger.info("Recebida requisição para buscar categoria por ID: {}", id);
        categoryAccessCounter.increment();
        
        Optional<CategoryDTO> category = categoryService.findById(id);
        
        if (category.isPresent()) {
            logger.info("Categoria encontrada com ID: {}", id);
            return ResponseEntity.ok(category.get());
        } else {
            logger.warn("Categoria não encontrada com ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    @Timed(value = "category.create", description = "Tempo para criar categoria")
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CreateCategoryRequest request) {
        logger.info("Recebida requisição para criar categoria com nome: {}", request.getName());
        
        try {
            CategoryDTO createdCategory = categoryService.create(request);
            categoryCreatedCounter.increment();
            
            logger.info("Categoria criada com sucesso. ID: {}", createdCategory.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
            
        } catch (RuntimeException e) {
            logger.error("Erro ao criar categoria: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @GetMapping("/{id}/exists")
    @Timed(value = "category.exists", description = "Tempo para verificar se categoria existe")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        logger.info("Recebida requisição para verificar existência da categoria com ID: {}", id);
        categoryAccessCounter.increment();
        
        boolean exists = categoryService.existsById(id);
        logger.info("Categoria com ID {} existe: {}", id, exists);
        
        return ResponseEntity.ok(exists);
    }
    
    @DeleteMapping("/{id}")
    @Timed(value = "category.delete", description = "Tempo para deletar categoria")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        logger.info("Recebida requisição para deletar categoria com ID: {}", id);
        
        try {
            categoryService.deleteById(id);
            logger.info("Categoria deletada com sucesso. ID: {}", id);
            return ResponseEntity.noContent().build();
            
        } catch (RuntimeException e) {
            logger.error("Erro ao deletar categoria: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}