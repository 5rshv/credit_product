package com.example.credit_product.dto;

import jakarta.persistence.*;

@Entity
@Table(name = "rule_stats")
public class RuleStatDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "rule_id", nullable = false, unique = true)
    private DynamicRuleDto rule;

    @Column(nullable = false)
    private long triggerCount;

    @Column(name = "last_triggered_timestamp")
    private Long lastTriggeredTimestamp;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DynamicRuleDto getRule() {
        return rule;
    }

    public void setRule(DynamicRuleDto rule) {
        this.rule = rule;
    }

    public long getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(long triggerCount) {
        this.triggerCount = triggerCount;
    }

    public Long getLastTriggeredTimestamp() {
        return lastTriggeredTimestamp;
    }

    public void setLastTriggeredTimestamp(Long lastTriggeredTimestamp) {
        this.lastTriggeredTimestamp = lastTriggeredTimestamp;
    }
}
