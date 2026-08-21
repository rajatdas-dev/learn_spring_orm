package com.example.springmvc.config;

import com.example.springmvc.exception.AsyncExceptionHandler;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "shopsphereTaskExecutor")
    public Executor taskExecutor(){

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);

        executor.setThreadNamePrefix("shopsphere-async-");

        executor.initialize();

        return  executor;
    }

    @Override
    public @Nullable Executor getAsyncExecutor() {
        return taskExecutor();
    }

    @Override
    public AsyncExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }
}
