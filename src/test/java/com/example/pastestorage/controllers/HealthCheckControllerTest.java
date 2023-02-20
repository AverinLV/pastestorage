package com.example.pastestorage.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HealthCheckControllerTest extends ControllerBaseTest{
    @Test
    public void shouldReturnOk() throws Exception {
        this.mockMvc.perform(get("/health_check/ping"))
                .andExpect(status().isOk());
    }
}
