package com.example.capstone2updated.DTO;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DealerServiceDTO {
    @NotEmpty(message = "the name field is required.")
    private String name;

    @NotNull(message = "the price field is required.")
    private Double price;
}
