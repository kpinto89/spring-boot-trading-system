package com.example.trading.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerTest {

    private static final String ORDER_PAYLOAD = "{\"symbol\":\"AAPL\",\"side\":\"BUY\",\"type\":\"LIMIT\",\"price\":180,\"quantity\":1}";
    private static final String REGISTER_PAYLOAD = "{\"email\":\"user1@example.com\",\"password\":\"Passw0rd!\",\"fullName\":\"User One\"}";

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test
    void placeOrder_requiresAuth() throws Exception {
        mvc.perform(post("/api/orders")
                .contentType("application/json")
                .content(ORDER_PAYLOAD))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void placeOrder_okAfterRegister() throws Exception {
        // Register user to satisfy FK constraints and get a valid JWT
        var regRes = mvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(REGISTER_PAYLOAD))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = mapper.readTree(regRes).get("token").asText();

        mvc.perform(post("/api/orders")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(ORDER_PAYLOAD))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.symbol").value("AAPL"));
    }
}
