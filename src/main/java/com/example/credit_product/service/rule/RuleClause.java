package com.example.credit_product.service.rule;

import com.example.credit_product.model.DynamicRule;
import jakarta.persistence.*;
import jakarta.persistence.Id;


import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rule_clauses")
public class RuleClause {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false)
    private String query;

    @ElementCollection
    @CollectionTable(name = "rule_clause_arguments", joinColumns = @JoinColumn(name = "clause_id"))
    @Column(name = "argument")
    private List<String> arguments;

    private boolean negate;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private DynamicRule rule;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}