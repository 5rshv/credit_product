package com.example.credit_product;

import com.example.credit_product.controller.InfoController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InfoController.class)
@TestPropertySource(properties = {
        "app.name=TestApp",
        "app.version=9.9.9"
})
class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Test
    void getInfo_returnsConfiguredValues() throws Exception {
        mockMvc.perform(get("/management/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestApp"))
                .andExpect(jsonPath("$.version").value("9.9.9"));
    }
}
