package com.example.credit_product;

import com.example.credit_product.controller.GlobalExceptionHandler;
import com.example.credit_product.controller.RecommendationController;
import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.dto.RecommendationResponse;
import com.example.credit_product.exception.UserNotFoundException;
import com.example.credit_product.repository.UserRepository;
import com.example.credit_product.service.RecommendationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RecommendationController.class)
@Import(GlobalExceptionHandler.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private RecommendationService recommendationService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void getRecommendations_ok() throws Exception {
        UUID userId = UUID.randomUUID();
        List<RecommendationDTO> recs = List.of(
                new RecommendationDTO("Product A", "prod-a", "text A"),
                new RecommendationDTO("Product B", "prod-b", "text B")
        );
        when(recommendationService.getRecommendations(userId))
                .thenReturn(recs);

        mockMvc.perform(get("/recommendation/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.recommendations", hasSize(2)))
                .andExpect(jsonPath("$.recommendations[0].name").value("Product A"))
                .andExpect(jsonPath("$.recommendations[1].id").value("prod-b"));
    }

    @Test
    void getRecommendations_userNotFound_returns404() throws Exception {
        UUID userId = UUID.randomUUID();
        when(recommendationService.getRecommendations(userId))
                .thenThrow(new UserNotFoundException(userId.toString()));

        mockMvc.perform(get("/recommendation/{userId}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message", containsString("не найден")));
    }
}
