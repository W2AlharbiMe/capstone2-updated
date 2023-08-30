package com.example.capstone2updated.Api.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseWithData<T> {
    private String message;
    private T data;
}
