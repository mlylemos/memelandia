package com.memelandia.user.service;

import com.memelandia.user.dto.CreateUserRequest;
import com.memelandia.user.dto.UserDTO;
import com.memelandia.user.model.User;
import com.memelandia.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    public List<UserDTO> findAll() {
        logger.info("Buscando todos os usuários");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<UserDTO> findById(Long id) {
        logger.info("Buscando usuário por ID: {}", id);
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public UserDTO create(CreateUserRequest request) {
        logger.info("Criando novo usuário com email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Tentativa de criar usuário com email já existente: {}", request.getEmail());
            throw new RuntimeException("Email já está em uso");
        }
        
        User user = new User(request.getName(), request.getEmail());
        User savedUser = userRepository.save(user);
        
        logger.info("Usuário criado com sucesso. ID: {}", savedUser.getId());
        return convertToDTO(savedUser);
    }
    
    public boolean existsById(Long id) {
        logger.info("Verificando existência do usuário com ID: {}", id);
        return userRepository.existsById(id);
    }
    
    public void deleteById(Long id) {
        logger.info("Deletando usuário com ID: {}", id);
        if (!userRepository.existsById(id)) {
            logger.warn("Tentativa de deletar usuário inexistente com ID: {}", id);
            throw new RuntimeException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
        logger.info("Usuário deletado com sucesso. ID: {}", id);
    }
    
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}