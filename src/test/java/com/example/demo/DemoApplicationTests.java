package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
class DemoApplicationTests {
    @Autowired
    WebTestClient webClient;

    @Test
    void envWhenPreflight() {
        this.webClient.options()
                .uri("https://example.com/actuator/env")
                .header("Origin", "http://localhost:8080")
                .header("Access-Control-Request-Method", "GET")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser
    void envWhenAuthenticated() {
        this.webClient.get()
                .uri("https://example.com/actuator/env")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void envWhenNotAuthenticated() {
        this.webClient.get()
                .uri("https://example.com/actuator/env")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
