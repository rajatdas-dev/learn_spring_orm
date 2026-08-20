package com.example.springmvc.exception;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, @Nullable Object... params) {

        log.error(
                "Async operation failed. Method: {}, Message: {}",
                method.getName(),
                ex.getMessage(),
                ex
        );
    }
}
