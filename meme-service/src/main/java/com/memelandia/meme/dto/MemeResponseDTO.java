package com.memelandia.meme.dto;

import com.memelandia.meme.model.Meme;
import java.time.LocalDateTime;

public class MemeResponseDTO {
    
    private Long id;
    private String nome;
    private String descricao;
    private String urlImagem;
    private Long usuarioId;
    private Long categoriaId;
    private LocalDateTime dataCadastro;
    
    public MemeResponseDTO() {}
    
    public MemeResponseDTO(Meme meme) {
        this.id = meme.getId();
        this.nome = meme.getNome();
        this.descricao = meme.getDescricao();
        this.urlImagem = meme.getUrlImagem();
        this.usuarioId = meme.getUsuarioId();
        this.categoriaId = meme.getCategoriaId();
        this.dataCadastro = meme.getDataCadastro();
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