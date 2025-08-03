package com.example.credit_product.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class InfoController {

    @Value("${app.name:CreditProductRecommendation}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String version;

    @GetMapping("/management/info")
    public Map<String, String> getInfo() {
        return Map.of(
                "name", appName,
                "version", version
        );
    }
}
