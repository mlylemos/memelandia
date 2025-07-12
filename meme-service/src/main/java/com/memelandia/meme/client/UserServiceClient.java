package com.memelandia.meme.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${app.services.user-service.url}")
public interface UserServiceClient {
    
    @GetMapping("/usuarios/{id}")
    Object buscarPorId(@PathVariable Long id);
}