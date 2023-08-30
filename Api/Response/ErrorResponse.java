package com.example.capstone2updated.Api.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String objectName;
    private String defaultMessage;
    private String field;
    private String code;
}
