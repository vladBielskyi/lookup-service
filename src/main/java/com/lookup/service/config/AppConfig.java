package com.lookup.service.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableScheduling
@Configuration
@AllArgsConstructor
public class AppConfig {

    private HikariDataSource hikariDataSource;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(hikariDataSource.getMaximumPoolSize());
    }
}
