package com.example.springmvc.response;
import com.example.springmvc.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private boolean success;
    private int status;
    private ErrorCode errorCode;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private String traceId;

}
