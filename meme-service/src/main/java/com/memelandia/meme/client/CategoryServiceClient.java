package com.memelandia.meme.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category-service", url = "${app.services.category-service.url}")
public interface CategoryServiceClient {
    
    @GetMapping("/api/categories/{id}")
    Object buscarPorId(@PathVariable Long id);
}