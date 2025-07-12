package com.memelandia.user.service;

import com.memelandia.user.dto.CreateUserRequest;
import com.memelandia.user.dto.UserDTO;
import com.memelandia.user.model.User;
import com.memelandia.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    public UserDTO createUser(CreateUserRequest request) {
        logger.info("Criando usuário com email: {}", request.email());
        
        if (userRepository.existsByEmail(request.email())) {
            logger.warn("Tentativa de criar usuário com email já existente: {}", request.email());
            throw new IllegalArgumentException("E-mail já cadastrado: " + request.email());
        }
        
        User user = new User(request.name(), request.email());
        User savedUser = userRepository.save(user);
        
        logger.info("Usuário criado com sucesso. ID: {}, Email: {}", savedUser.getId(), savedUser.getEmail());
        
        return mapToDTO(savedUser);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(Long id) {
        logger.debug("Buscando usuário por ID: {}", id);
        
        return userRepository.findById(id)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        logger.debug("Buscando usuário por email: {}", email);
        
        return userRepository.findByEmail(email)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        logger.debug("Buscando todos os usuários. Página: {}, Tamanho: {}", 
                     pageable.getPageNumber(), pageable.getPageSize());
        
        return userRepository.findAll(pageable)
                .map(this::mapToDTO);
    }
    
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        logger.debug("Verificando existência do usuário com ID: {}", id);
        return userRepository.existsById(id);
    }
    
    @Transactional(readOnly = true)
    public long countTotalUsers() {
        return userRepository.countTotalUsers();
    }
    
    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}