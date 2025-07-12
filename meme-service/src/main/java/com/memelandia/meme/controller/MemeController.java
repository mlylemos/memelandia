package com.memelandia.meme.controller;

import com.memelandia.meme.dto.MemeRequestDTO;
import com.memelandia.meme.dto.MemeResponseDTO;
import com.memelandia.meme.service.MemeService;
import io.micrometer.core.annotation.Timed;
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
@RequestMapping("/memes")
public class MemeController {
    
    private static final Logger logger = LoggerFactory.getLogger(MemeController.class);
    
    @Autowired
    private MemeService memeService;
    
    @PostMapping
    @Timed(value = "meme.create", description = "Tempo para criar um meme")
    public ResponseEntity<MemeResponseDTO> criarMeme(@Valid @RequestBody MemeRequestDTO request) {
        logger.info("Requisição para criar meme: {}", request.getNome());
        
        try {
            MemeResponseDTO meme = memeService.criarMeme(request);
            logger.info("Meme criado com sucesso - ID: {}", meme.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(meme);
        } catch (Exception e) {
            logger.error("Erro ao criar meme: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping
    @Timed(value = "meme.list", description = "Tempo para listar todos os memes")
    public ResponseEntity<List<MemeResponseDTO>> listarTodos() {
        logger.info("Requisição para listar todos os memes");
        
        List<MemeResponseDTO> memes = memeService.listarTodos();
        logger.info("Retornando {} memes", memes.size());
        
        return ResponseEntity.ok(memes);
    }
    
    @GetMapping("/{id}")
    @Timed(value = "meme.get", description = "Tempo para buscar meme por ID")
    public ResponseEntity<MemeResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("Requisição para buscar meme por ID: {}", id);
        
        Optional<MemeResponseDTO> meme = memeService.buscarPorId(id);
        if (meme.isPresent()) {
            logger.info("Meme encontrado: {}", meme.get().getNome());
            return ResponseEntity.ok(meme.get());
        }
        
        logger.warn("Meme não encontrado com ID: {}", id);
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/usuario/{usuarioId}")
    @Timed(value = "meme.get.by.user", description = "Tempo para buscar memes por usuário")
    public ResponseEntity<List<MemeResponseDTO>> buscarPorUsuario(@PathVariable Long usuarioId) {
        logger.info("Requisição para buscar memes do usuário: {}", usuarioId);
        
        List<MemeResponseDTO> memes = memeService.buscarPorUsuario(usuarioId);
        logger.info("Retornando {} memes para o usuário {}", memes.size(), usuarioId);
        
        return ResponseEntity.ok(memes);
    }
    
    @GetMapping("/categoria/{categoriaId}")
    @Timed(value = "meme.get.by.category", description = "Tempo para buscar memes por categoria")
    public ResponseEntity<List<MemeResponseDTO>> buscarPorCategoria(@PathVariable Long categoriaId) {
        logger.info("Requisição para buscar memes da categoria: {}", categoriaId);
        
        List<MemeResponseDTO> memes = memeService.buscarPorCategoria(categoriaId);
        logger.info("Retornando {} memes para a categoria {}", memes.size(), categoriaId);
        
        return ResponseEntity.ok(memes);
    }
    
    @GetMapping("/meme-do-dia")
    @Timed(value = "meme.daily", description = "Tempo para buscar meme do dia")
    public ResponseEntity<MemeResponseDTO> memeDoDia() {
        logger.info("Requisição para buscar meme do dia");
        
        Optional<MemeResponseDTO> meme = memeService.memeDoDia();
        if (meme.isPresent()) {
            logger.info("Meme do dia retornado: {}", meme.get().getNome());
            return ResponseEntity.ok(meme.get());
        }
        
        logger.warn("Nenhum meme encontrado para o meme do dia");
        return ResponseEntity.notFound().build();
    }
}