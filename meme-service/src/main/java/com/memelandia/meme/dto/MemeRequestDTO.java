package com.memelandia.meme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MemeRequestDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;
    
    @NotBlank(message = "URL da imagem é obrigatória")
    private String urlImagem;
    
    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
    
    @NotNull(message = "ID da categoria é obrigatório")
    private Long categoriaId;
    
    public MemeRequestDTO() {}
    
    public MemeRequestDTO(String nome, String descricao, String urlImagem, Long usuarioId, Long categoriaId) {
        this.nome = nome;
        this.descricao = descricao;
        this.urlImagem = urlImagem;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
    }
    
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
}