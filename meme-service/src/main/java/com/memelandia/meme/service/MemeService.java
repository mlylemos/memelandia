package com.memelandia.meme.service;

import com.memelandia.meme.client.CategoryServiceClient;
import com.memelandia.meme.client.UserServiceClient;
import com.memelandia.meme.dto.MemeRequestDTO;
import com.memelandia.meme.dto.MemeResponseDTO;
import com.memelandia.meme.model.Meme;
import com.memelandia.meme.repository.MemeRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemeService {
    
    private static final Logger logger = LoggerFactory.getLogger(MemeService.class);
    
    @Autowired
    private MemeRepository memeRepository;
    
    @Autowired
    private UserServiceClient userServiceClient;
    
    @Autowired
    private CategoryServiceClient categoryServiceClient;
    
    public MemeResponseDTO criarMeme(MemeRequestDTO request) {
        logger.info("Iniciando criação de meme: {}", request.getNome());
        
        try {
            validarUsuario(request.getUsuarioId());
            validarCategoria(request.getCategoriaId());
            
            Meme meme = new Meme(
                request.getNome(),
                request.getDescricao(),
                request.getUrlImagem(),
                request.getUsuarioId(),
                request.getCategoriaId()
            );
            
            Meme memeSalvo = memeRepository.save(meme);
            logger.info("Meme criado com sucesso - ID: {}", memeSalvo.getId());
            
            return new MemeResponseDTO(memeSalvo);
            
        } catch (Exception e) {
            logger.error("Erro ao criar meme: {}", e.getMessage());
            throw e;
        }
    }
    
    public List<MemeResponseDTO> listarTodos() {
        logger.info("Listando todos os memes");
        
        List<Meme> memes = memeRepository.findAll();
        logger.info("Encontrados {} memes", memes.size());
        
        return memes.stream()
                .map(MemeResponseDTO::new)
                .collect(Collectors.toList());
    }
    
    public Optional<MemeResponseDTO> buscarPorId(Long id) {
        logger.info("Buscando meme por ID: {}", id);
        
        Optional<Meme> meme = memeRepository.findById(id);
        if (meme.isPresent()) {
            logger.info("Meme encontrado: {}", meme.get().getNome());
            return Optional.of(new MemeResponseDTO(meme.get()));
        }
        
        logger.warn("Meme não encontrado com ID: {}", id);
        return Optional.empty();
    }
    
    public List<MemeResponseDTO> buscarPorUsuario(Long usuarioId) {
        logger.info("Buscando memes do usuário: {}", usuarioId);
        
        List<Meme> memes = memeRepository.findByUsuarioId(usuarioId);
        logger.info("Encontrados {} memes para o usuário {}", memes.size(), usuarioId);
        
        return memes.stream()
                .map(MemeResponseDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<MemeResponseDTO> buscarPorCategoria(Long categoriaId) {
        logger.info("Buscando memes da categoria: {}", categoriaId);
        
        List<Meme> memes = memeRepository.findByCategoriaId(categoriaId);
        logger.info("Encontrados {} memes para a categoria {}", memes.size(), categoriaId);
        
        return memes.stream()
                .map(MemeResponseDTO::new)
                .collect(Collectors.toList());
    }
    
    public Optional<MemeResponseDTO> memeDoDia() {
        logger.info("Buscando meme do dia");
        
        Optional<Meme> memeAleatorio = memeRepository.findRandomMeme();
        if (memeAleatorio.isPresent()) {
            logger.info("Meme do dia selecionado: {}", memeAleatorio.get().getNome());
            return Optional.of(new MemeResponseDTO(memeAleatorio.get()));
        }
        
        logger.warn("Nenhum meme encontrado para o meme do dia");
        return Optional.empty();
    }
    
    private void validarUsuario(Long usuarioId) {
        try {
            logger.debug("Validando usuário: {}", usuarioId);
            userServiceClient.buscarPorId(usuarioId);
            logger.debug("Usuário válido: {}", usuarioId);
        } catch (FeignException.NotFound e) {
            logger.error("Usuário não encontrado: {}", usuarioId);
            throw new RuntimeException("Usuário não encontrado com ID: " + usuarioId);
        } catch (Exception e) {
            logger.error("Erro ao validar usuário {}: {}", usuarioId, e.getMessage());
            throw new RuntimeException("Erro ao validar usuário: " + e.getMessage());
        }
    }
    
    private void validarCategoria(Long categoriaId) {
        try {
            logger.debug("Validando categoria: {}", categoriaId);
            categoryServiceClient.buscarPorId(categoriaId);
            logger.debug("Categoria válida: {}", categoriaId);
        } catch (FeignException.NotFound e) {
            logger.error("Categoria não encontrada: {}", categoriaId);
            throw new RuntimeException("Categoria não encontrada com ID: " + categoriaId);
        } catch (Exception e) {
            logger.error("Erro ao validar categoria {}: {}", categoriaId, e.getMessage());
            throw new RuntimeException("Erro ao validar categoria: " + e.getMessage());
        }
    }
}