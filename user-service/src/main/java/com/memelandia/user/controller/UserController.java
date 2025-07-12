package com.memelandia.user.controller;

import com.memelandia.user.dto.CreateUserRequest;
import com.memelandia.user.dto.UserDTO;
import com.memelandia.user.service.UserService;
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
@RequestMapping("/api/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    private final Counter userCreatedCounter;
    private final Counter userAccessCounter;
    
    public UserController(MeterRegistry meterRegistry) {
        this.userCreatedCounter = Counter.builder("users_created_total")
                .description("Total de usuários criados")
                .register(meterRegistry);
        
        this.userAccessCounter = Counter.builder("users_accessed_total")
                .description("Total de acessos aos usuários")
                .register(meterRegistry);
    }
    
    @GetMapping
    @Timed(value = "user.find.all", description = "Tempo para buscar todos os usuários")
    public ResponseEntity<List<UserDTO>> findAll() {
        logger.info("Recebida requisição para buscar todos os usuários");
        userAccessCounter.increment();
        
        List<UserDTO> users = userService.findAll();
        logger.info("Retornando {} usuários", users.size());
        
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    @Timed(value = "user.find.by.id", description = "Tempo para buscar usuário por ID")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        logger.info("Recebida requisição para buscar usuário por ID: {}", id);
        userAccessCounter.increment();
        
        Optional<UserDTO> user = userService.findById(id);
        
        if (user.isPresent()) {
            logger.info("Usuário encontrado com ID: {}", id);
            return ResponseEntity.ok(user.get());
        } else {
            logger.warn("Usuário não encontrado com ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    @Timed(value = "user.create", description = "Tempo para criar usuário")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody CreateUserRequest request) {
        logger.info("Recebida requisição para criar usuário com email: {}", request.getEmail());
        
        try {
            UserDTO createdUser = userService.create(request);
            userCreatedCounter.increment();
            
            logger.info("Usuário criado com sucesso. ID: {}", createdUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
            
        } catch (RuntimeException e) {
            logger.error("Erro ao criar usuário: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @GetMapping("/{id}/exists")
    @Timed(value = "user.exists", description = "Tempo para verificar se usuário existe")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        logger.info("Recebida requisição para verificar existência do usuário com ID: {}", id);
        userAccessCounter.increment();
        
        boolean exists = userService.existsById(id);
        logger.info("Usuário com ID {} existe: {}", id, exists);
        
        return ResponseEntity.ok(exists);
    }
    
    @DeleteMapping("/{id}")
    @Timed(value = "user.delete", description = "Tempo para deletar usuário")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        logger.info("Recebida requisição para deletar usuário com ID: {}", id);
        
        try {
            userService.deleteById(id);
            logger.info("Usuário deletado com sucesso. ID: {}", id);
            return ResponseEntity.noContent().build();
            
        } catch (RuntimeException e) {
            logger.error("Erro ao deletar usuário: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}