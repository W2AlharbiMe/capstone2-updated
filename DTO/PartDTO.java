package com.example.capstone2updated.DTO;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true) // this is used to take default initialized values into account.
public class PartDTO {
    @NotEmpty(message = "the name field is required.")
    private String name;

    private String description = "";

    @NotNull(message = "the purchase price field is required.")
    private Double purchasePrice;

    @NotNull(message = "the is used field is required.")
    private Boolean isUsed = false;
}
