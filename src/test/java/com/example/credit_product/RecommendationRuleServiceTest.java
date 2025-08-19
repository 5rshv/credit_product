package com.example.credit_product;

import com.example.credit_product.dto.CreateRuleRequest;
import com.example.credit_product.dto.RecommendationRuleDTO;
import com.example.credit_product.model.RecommendationRule;
import com.example.credit_product.model.RuleQuery;
import com.example.credit_product.repository.RecommendationRuleRepository;
import com.example.credit_product.service.RecommendationRuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationRuleServiceTest {

    @Mock
    private RecommendationRuleRepository ruleRepository;

    @InjectMocks
    private RecommendationRuleService service;

    @Test
    void createRule_success() {
        CreateRuleRequest req = CreateRuleRequest.builder()
                .name("rule-1")
                .description("age > 18")
                .productType("CREDIT_CARD")
                .queries(List.of(new RuleQuery("USER_OF", List.of("CREDIT_CARD"), false)))
                .build();

        when(ruleRepository.existsByName("rule-1")).thenReturn(false);
        when(ruleRepository.save(any(RecommendationRule.class)))
                .thenAnswer(inv -> {
                    RecommendationRule r = inv.getArgument(0);
                    r.setId(UUID.randomUUID());
                    return r;
                });

        RecommendationRuleDTO dto = service.createRule(req);

        assertNotNull(dto.getId());
        assertEquals("rule-1", dto.getName());
        assertEquals("age > 18", dto.getDescription());
        assertEquals("CREDIT_CARD", dto.getProductType());
        assertTrue(dto.isActive());
        assertEquals(1, dto.getQueries().size());

        verify(ruleRepository).existsByName("rule-1");
        verify(ruleRepository).save(any(RecommendationRule.class));
    }

    @Test
    void createRule_duplicateName_throws() {
        CreateRuleRequest req = CreateRuleRequest.builder()
                .name("rule-1")
                .description("d")
                .productType("T")
                .queries(List.of(new RuleQuery("USER_OF", List.of("T"), false)))
                .build();

        when(ruleRepository.existsByName("rule-1")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.createRule(req));
        verify(ruleRepository).existsByName("rule-1");
        verify(ruleRepository, never()).save(any());
    }

    @Test
    void getAllRules_mapsToDTOList() {
        RecommendationRule r1 = RecommendationRule.builder()
                .id(UUID.randomUUID())
                .name("r1")
                .description("d1")
                .productType("T1")
                .queries(List.of())
                .active(true)
                .build();
        RecommendationRule r2 = RecommendationRule.builder()
                .id(UUID.randomUUID())
                .name("r2")
                .description("d2")
                .productType("T2")
                .queries(List.of())
                .active(false)
                .build();

        when(ruleRepository.findAll()).thenReturn(List.of(r1, r2));

        var list = service.getAllRules();

        assertEquals(2, list.size());
        assertEquals("r1", list.get(0).getName());
        assertEquals("r2", list.get(1).getName());
        verify(ruleRepository).findAll();
    }

}
