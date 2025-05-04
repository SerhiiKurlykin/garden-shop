package com.predators.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class SchedulerConfig {

    @Bean
    public Executor pool() {
        return Executors.newCachedThreadPool();
    }
}
