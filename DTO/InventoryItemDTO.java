package com.example.capstone2updated.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryItemDTO {

    @Positive(message = "the car id must be positive number.")
    private Integer carId;


    @Positive(message = "the part id must be positive number.")
    private Integer partId;


    @NotNull(message = "the inventory id field is required.")
    @Positive(message = "the inventory id must be positive number.")
    private Integer inventoryId;


    @NotEmpty(message = "the type field is required.")
    @Pattern(message = "the type can only be `car` or `part`", regexp = "(?i)\\b(car|part)\\b?")
    private String type; // car or part ?


    @NotNull(message = "the quantity field is required.")
    @Positive(message = "the quantity must be positive number.")
    @Min(value = 5, message = "the quantity must be at least 5.")
    private Integer quantity; // you can not store more than < maxCapacity

}
