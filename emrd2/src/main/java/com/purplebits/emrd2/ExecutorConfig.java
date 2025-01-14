package com.purplebits.emrd2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfig {

    @Bean(name = "ocrExecutor")
    public ThreadPoolTaskExecutor ocrExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("OCR-");
        executor.initialize();
        return executor;
    }
    
    @Bean(name = "ocrRefinementExecutor")
    public ThreadPoolTaskExecutor ocrRefinementExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("OCR-Refinement-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "encryptionExecutor")
    public ThreadPoolTaskExecutor encryptionExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("Encryption-");
        executor.initialize();
        return executor;
    }
}
