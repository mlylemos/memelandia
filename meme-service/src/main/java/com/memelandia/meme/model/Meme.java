package com.memelandia.meme.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "memes")
public class Meme {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, length = 500)
    private String descricao;
    
    @Column(nullable = false)
    private String urlImagem;
    
    @Column(nullable = false)
    private Long usuarioId;
    
    @Column(nullable = false)
    private Long categoriaId;
    
    @Column(nullable = false)
    private LocalDateTime dataCadastro;
    
    public Meme() {
        this.dataCadastro = LocalDateTime.now();
    }
    
    public Meme(String nome, String descricao, String urlImagem, Long usuarioId, Long categoriaId) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.urlImagem = urlImagem;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}