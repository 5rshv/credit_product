package com.example.credit_product;

import com.example.credit_product.controller.RuleController;
import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.dto.RuleQueryDTO;
import com.example.credit_product.service.DynamicRuleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RuleController.class)
@Import(ObjectMapper.class)
class RuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DynamicRuleService ruleService;

    @Autowired
    private ObjectMapper om;

    @Test
    void createRule_returns201AndBody() throws Exception {
        DynamicRuleDTO req = new DynamicRuleDTO();
        req.setProductName("Prod");
        req.setProductId("prod-1");
        req.setProductText("Text");
        RuleQueryDTO q = new RuleQueryDTO();
        q.setQuery("USER_OF");
        q.setArguments(List.of("CARD"));
        q.setNegate(false);
        req.setRule(List.of(q));

        DynamicRuleDTO resp = new DynamicRuleDTO();
        resp.setId("id-1");
        resp.setProductName(req.getProductName());
        resp.setProductId(req.getProductId());
        resp.setProductText(req.getProductText());
        resp.setRule(req.getRule());

        when(ruleService.createRule(any(DynamicRuleDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("id-1"))
                .andExpect(jsonPath("$.product_id").value("prod-1"))
                .andExpect(jsonPath("$.product_name").value("Prod"));
    }

    @Test
    void getAllRules_returns200AndList() throws Exception {
        when(ruleService.getAllRules()).thenReturn(List.of(new DynamicRuleDTO(), new DynamicRuleDTO()));

        mockMvc.perform(get("/rule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void deleteRule_returns204() throws Exception {
        doNothing().when(ruleService).deleteRuleByProductId("prod-1");

        mockMvc.perform(delete("/rule/{productId}", "prod-1"))
                .andExpect(status().isNoContent());
    }
}
