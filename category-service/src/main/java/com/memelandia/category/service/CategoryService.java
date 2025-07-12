package com.memelandia.category.service;

import com.memelandia.category.dto.CategoryDTO;
import com.memelandia.category.dto.CreateCategoryRequest;
import com.memelandia.category.model.Category;
import com.memelandia.category.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    public List<CategoryDTO> findAll() {
        logger.info("Buscando todas as categorias");
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<CategoryDTO> findById(Long id) {
        logger.info("Buscando categoria por ID: {}", id);
        return categoryRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public CategoryDTO create(CreateCategoryRequest request) {
        logger.info("Criando nova categoria com nome: {}", request.getName());
        
        if (categoryRepository.existsByName(request.getName())) {
            logger.warn("Tentativa de criar categoria com nome já existente: {}", request.getName());
            throw new RuntimeException("Nome da categoria já está em uso");
        }
        
        Category category = new Category(request.getName(), request.getDescription());
        Category savedCategory = categoryRepository.save(category);
        
        logger.info("Categoria criada com sucesso. ID: {}", savedCategory.getId());
        return convertToDTO(savedCategory);
    }
    
    public boolean existsById(Long id) {
        logger.info("Verificando existência da categoria com ID: {}", id);
        return categoryRepository.existsById(id);
    }
    
    public void deleteById(Long id) {
        logger.info("Deletando categoria com ID: {}", id);
        if (!categoryRepository.existsById(id)) {
            logger.warn("Tentativa de deletar categoria inexistente com ID: {}", id);
            throw new RuntimeException("Categoria não encontrada");
        }
        categoryRepository.deleteById(id);
        logger.info("Categoria deletada com sucesso. ID: {}", id);
    }
    
    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt()
        );
    }
}