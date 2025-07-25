package com.memelandia.meme.repository;

import com.memelandia.meme.model.Meme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemeRepository extends JpaRepository<Meme, Long> {
    
    List<Meme> findByUsuarioId(Long usuarioId);
    
    List<Meme> findByCategoriaId(Long categoriaId);
    
    @Query(value = "SELECT * FROM memes ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Meme> findRandomMeme();
}