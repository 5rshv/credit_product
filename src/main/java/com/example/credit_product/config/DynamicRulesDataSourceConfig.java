package com.example.credit_product.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.credit_product.repository",
        entityManagerFactoryRef = "dynamicRulesEntityManagerFactory",
        transactionManagerRef = "dynamicRulesTransactionManager"
)
public class DynamicRulesDataSourceConfig {

    @Value("${spring.dynamic-rules.datasource.url:jdbc:h2:mem:dynamic_rules_db}")
    private String jdbcUrl;

    @Value("${spring.dynamic-rules.datasource.driver-class-name:org.h2.Driver}")
    private String driverClassName;

    @Value("${spring.dynamic-rules.datasource.username:sa}")
    private String username;

    @Value("${spring.dynamic-rules.datasource.password:}")
    private String password;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dynamicRulesDataSource());
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.yaml");
        return liquibase;
    }

    @Bean
    public DataSource dynamicRulesDataSource() {
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean dynamicRulesEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dynamicRulesDataSource());
        em.setPackagesToScan("com.example.credit_product.model");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean
    public PlatformTransactionManager dynamicRulesTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(dynamicRulesEntityManagerFactory().getObject());
        return transactionManager;
    }
}