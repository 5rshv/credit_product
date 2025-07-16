package com.example.credit_product.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rule_clauses")
public class RuleClause {
    @Id
    @GeneratedValue
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