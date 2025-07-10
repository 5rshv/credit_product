//package com.example.credit_product;
//
//import com.example.credit_product.config.RecommendationController;
//import com.example.credit_product.dto.RecommendationDTO;
//import com.example.credit_product.repository.UserRepository;
//import com.example.credit_product.service.RecommendationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//public class RecommendationControllerTest {
//
//    @Mock
//    private RecommendationService recommendationService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private RecommendationController recommendationController;
//
//    private MockMvc mockMvc;
//
//    private final UUID testUserId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
//    }
//
//    @Test
//    public void getRecommendations_ShouldReturnRecommendations() throws Exception {
//        // Подготовка тестовых данных
//        List<RecommendationDTO> mockRecommendations = List.of(
//                new RecommendationDTO("Invest 500", "147f6a0f-3b91-413b-ab99-87f081d60d5a", "Test description")
//        );
//
//        // Настройка моков
//        when(userRepository.existsById(testUserId)).thenReturn(true);
//        when(recommendationService.getRecommendations(testUserId)).thenReturn(mockRecommendations);
//
//        // Выполнение запроса и проверки
//        mockMvc.perform(get("/recommendation/{userId}", testUserId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$.userId").value(testUserId.toString())) // Изменено с user_id на userId
//                .andExpect(jsonPath("$.recommendations[0].name").value("Invest 500"))
//                .andExpect(jsonPath("$.recommendations[0].id").value("147f6a0f-3b91-413b-ab99-87f081d60d5a"));
//
//        // Проверка вызовов
//        verify(userRepository, times(1)).existsById(testUserId);
//        verify(recommendationService, times(1)).getRecommendations(testUserId);
//    }
//
//    @Test
//    public void getRecommendations_ShouldReturnEmptyList() throws Exception {
//        when(userRepository.existsById(testUserId)).thenReturn(true);
//        when(recommendationService.getRecommendations(testUserId)).thenReturn(List.of());
//
//        mockMvc.perform(get("/recommendation/{userId}", testUserId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$.userId").value(testUserId.toString())) // Изменено с user_id на userId
//                .andExpect(jsonPath("$.recommendations").isEmpty());
//    }
//
//}