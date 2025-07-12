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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    private final Counter userCreatedCounter;
    private final Counter userNotFoundCounter;
    
    public UserController(MeterRegistry meterRegistry) {
        this.userCreatedCounter = Counter.builder("users_created_total")
                .description("Total de usuários criados")
                .register(meterRegistry);
        
        this.userNotFoundCounter = Counter.builder("users_not_found_total")
                .description("Total de usuários não encontrados")
                .register(meterRegistry);
    }
    
    @PostMapping
    @Timed(value = "user_creation_time", description = "Tempo para criar usuário")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        logger.info("Recebida requisição para criar usuário: {}", request.email());
        
        try {
            UserDTO createdUser = userService.createUser(request);
            userCreatedCounter.increment();
            
            logger.info("Usuário criado com sucesso. ID: {}", createdUser.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Erro ao criar usuário: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            logger.error("Erro interno ao criar usuário", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    @Timed(value = "user_fetch_time", description = "Tempo para buscar usuário")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        logger.debug("Recebida requisição para buscar usuário por ID: {}", id);
        
        Optional<UserDTO> user = userService.findById(id);
        
        if (user.isPresent()) {
            logger.debug("Usuário encontrado: {}", id);
            return ResponseEntity.ok(user.get());
        } else {
            logger.warn("Usuário não encontrado: {}", id);
            userNotFoundCounter.increment();
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    @Timed(value = "users_list_time", description = "Tempo para listar usuários")
    public ResponseEntity<Page<UserDTO>> getUsers(Pageable pageable) {
        logger.debug("Recebida requisição para listar usuários");
        
        Page<UserDTO> users = userService.findAll(pageable);
        
        logger.debug("Retornando {} usuários", users.getTotalElements());
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/email/{email}")
    @Timed(value = "user_fetch_by_email_time", description = "Tempo para buscar usuário por email")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        logger.debug("Recebida requisição para buscar usuário por email: {}", email);
        
        Optional<UserDTO> user = userService.findByEmail(email);
        
        if (user.isPresent()) {
            logger.debug("Usuário encontrado por email: {}", email);
            return ResponseEntity.ok(user.get());
        } else {
            logger.warn("Usuário não encontrado por email: {}", email);
            userNotFoundCounter.increment();
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/exists")
    @Timed(value = "user_exists_check_time", description = "Tempo para verificar existência do usuário")
    public ResponseEntity<Boolean> userExists(@PathVariable Long id) {
        logger.debug("Verificando existência do usuário: {}", id);
        
        boolean exists = userService.existsById(id);
        
        logger.debug("Usuário {} existe: {}", id, exists);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        long count = userService.countTotalUsers();
        logger.debug("Total de usuários: {}", count);
        return ResponseEntity.ok(count);
    }
}