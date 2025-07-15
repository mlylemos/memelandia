package com.memelandia.meme;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTest {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String userServiceUrl = "http://localhost:8081/api/users";
    private final String categoryServiceUrl = "http://localhost:8082/api/categories";
    private final String memeServiceUrl = "http://localhost:8083/memes";

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    @Test
    public void deveCriarUsuarioCategoriaEMeme() {
        Map<String, String> usuario = Map.of(
                "name", "Emily Tester",
                "email", "emily_" + System.currentTimeMillis() + "@example.com"
        );
        HttpEntity<Map<String, String>> usuarioRequest = new HttpEntity<>(usuario, jsonHeaders());
        ResponseEntity<Map> userResponse = restTemplate.postForEntity(userServiceUrl, usuarioRequest, Map.class);
        assertEquals(HttpStatus.CREATED, userResponse.getStatusCode(), "Falha ao criar usuário");
        Long usuarioId = Long.valueOf(userResponse.getBody().get("id").toString());
        System.out.println("Usuário criado: " + userResponse.getBody());

        Map<String, String> categoria = Map.of(
                "name", "Animais " + System.currentTimeMillis(),
                "description", "Memes de bichinhos"
        );
        HttpEntity<Map<String, String>> categoriaRequest = new HttpEntity<>(categoria, jsonHeaders());
        ResponseEntity<Map> categoriaResponse = restTemplate.postForEntity(categoryServiceUrl, categoriaRequest, Map.class);
        assertEquals(HttpStatus.CREATED, categoriaResponse.getStatusCode(), "Falha ao criar categoria");
        Long categoriaId = Long.valueOf(categoriaResponse.getBody().get("id").toString());
        System.out.println("Categoria criada: " + categoriaResponse.getBody());

        Map<String, Object> meme = Map.of(
                "nome", "Gato triste",
                "descricao", "Um gato desanimado com a segunda-feira",
                "urlImagem", "https://i.pinimg.com/736x/bc/1e/f7/bc1ef7b4113ecf9ef46fbfc911154ed6.jpg",
                "usuarioId", usuarioId,
                "categoriaId", categoriaId
        );
        HttpEntity<Map<String, Object>> memeRequest = new HttpEntity<>(meme, jsonHeaders());

        try {
            ResponseEntity<Map> memeResponse = restTemplate.postForEntity(memeServiceUrl, memeRequest, Map.class);
            assertEquals(HttpStatus.CREATED, memeResponse.getStatusCode(), "Falha ao criar meme");
            System.out.println("Meme criado: " + memeResponse.getBody());
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.out.println("Erro HTTP: " + e.getStatusCode());
            System.out.println("Body do erro: " + e.getResponseBodyAsString());
            throw e;
        }
    } 

} 
