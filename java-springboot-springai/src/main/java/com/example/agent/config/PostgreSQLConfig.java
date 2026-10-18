package com.example.agent.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Configuration pour PostgreSQL (activée avec le profil 'prod').
 */
@Configuration
@Profile("prod")
public class PostgreSQLConfig {
    
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/agentdb")
                .username("postgres")
                .password("postgres")
                .build();
    }
}
