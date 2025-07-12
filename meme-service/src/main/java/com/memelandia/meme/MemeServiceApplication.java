package com.memelandia.meme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MemeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemeServiceApplication.class, args);
    }
}