package ru.javabegin.micro.planner.apigateway.infrastructure.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import webapp.apigateway.ApiGatewayApplication;

@SpringBootTest(classes = ApiGatewayApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiGatewayApplicationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void testAnalyzerRoute() {
    webTestClient.get().uri("/analyzer/hobbies")
      .exchange()
      .expectStatus().isOk();
  }

  @Test
  public void testGeneratorRoute() {
    webTestClient.get().uri("/generator/templates")
      .exchange()
      .expectStatus().isOk();
  }
}

