package com.example.credit_product.service.rule;
import com.example.credit_product.dto.DynamicRuleDTO;
import com.example.credit_product.dto.RuleDto;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import org.springframework.data.annotation.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dynamic_rules")
public class DynamicRule {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String productText;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RuleClause> ruleClauses = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void createRule(@Valid DynamicRuleDTO dto) {

    }

    public List<DynamicRuleDTO> getAllRules() {
        return List.of();
    }

    public void deleteRuleByProductId(String productId) {

    }

    public List<RuleDto> getRuleStats() {

        return List.of();
    }
}