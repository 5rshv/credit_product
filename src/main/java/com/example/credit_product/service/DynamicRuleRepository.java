package com.example.credit_product.service;

import com.example.credit_product.model.DynamicRule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DynamicRuleRepository extends CrudRepository<DynamicRule, UUID> {
}