package com.example.credit_product;

import com.example.credit_product.controller.GlobalExceptionHandler;
import com.example.credit_product.controller.RecommendationRuleController;
import com.example.credit_product.dto.CreateRuleRequest;
import com.example.credit_product.dto.RecommendationRuleDTO;
import com.example.credit_product.model.RuleQuery;
import com.example.credit_product.service.RecommendationRuleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RecommendationRuleController.class)
@Import(GlobalExceptionHandler.class)
class RecommendationRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationRuleService recommendationRuleService;

    @Autowired
    private ObjectMapper om;

    @Test
    void createRule_returns201AndDto() throws Exception {
        RecommendationRuleDTO dto = RecommendationRuleDTO.builder()
                .id(UUID.randomUUID())
                .name("rule-1")
                .description("age > 18")
                .productType("CREDIT_CARD")
                .queries(List.of(new RuleQuery("USER_OF", List.of("CREDIT_CARD"), false)))
                .active(true)
                .build();

        when(recommendationRuleService.createRule(any(CreateRuleRequest.class)))
                .thenReturn(dto);

        String body = """
                {
                  "name": "rule-1",
                  "description": "age > 18",
                  "productType": "CREDIT_CARD",
                  "queries": [
                    { "query": "USER_OF", "arguments": ["CREDIT_CARD"], "negate": false }
                  ]
                }
                """;

        mockMvc.perform(post("/recommendation-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("rule-1"));
    }

    @Test
    void getAllRules_returns200AndList() throws Exception {
        List<RecommendationRuleDTO> list = List.of(
                RecommendationRuleDTO.builder()
                        .id(UUID.randomUUID())
                        .name("r1")
                        .description("cond1")
                        .productType("TYPE1")
                        .queries(List.of())
                        .active(true)
                        .build(),
                RecommendationRuleDTO.builder()
                        .id(UUID.randomUUID())
                        .name("r2")
                        .description("cond2")
                        .productType("TYPE2")
                        .queries(List.of())
                        .active(false)
                        .build()
        );
        when(recommendationRuleService.getAllRules()).thenReturn(list);

        mockMvc.perform(get("/recommendation-rules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void deleteRule_returns204() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(recommendationRuleService).deleteRule(id);

        mockMvc.perform(delete("/recommendation-rules/{ruleId}", id))
                .andExpect(status().isNoContent());
    }
}
