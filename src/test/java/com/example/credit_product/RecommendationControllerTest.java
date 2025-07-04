package com.example.credit_product;

import com.example.credit_product.controller.RecommendationController;
import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.dto.RecommendationResponse;
import com.example.credit_product.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendationController.class)
public class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;

    @Test
    public void getRecommendations_ShouldReturnRecommendations() throws Exception {
        String userId = "test-user-id";
        RecommendationResponse mockResponse = new RecommendationResponse(
            userId,
            Arrays.asList(new RecommendationDTO("Test Product", "test-id", "Test Description"))
        );

        when(recommendationService.getRecommendations(anyString())).thenReturn(mockResponse);

        mockMvc.perform(get("/recommendation/" + userId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.userId").value(userId))
               .andExpect(jsonPath("$.recommendations[0].name").value("Test Product"));
    }
}
