package com.example.capstone2updated.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateSalesPersonDTO {
    @NotEmpty(message = "the name field is required.")
    private String name;


    @NotNull(message = "the salary field is required.")
    @Positive(message = "the salary must be positive number.")
    private Double salary;

    @NotNull(message = "the active field is required.")
    private Boolean active;
}
