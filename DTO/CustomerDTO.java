package com.example.capstone2updated.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDTO {

    @NotEmpty(message = "the name field is required.")
    @Size(min = 3, message = "the name must be at least 3 chars.")
    private String name;

    @NotEmpty(message = "the phone number field is required.")
    @Pattern(message = "the entered phone number is invalid.", regexp = "^(009665|9665|\\+9665|05|5)(5|0|3|6|4|9|1|8|7)([0-9]{7})$")
    private String phoneNumber;

    @NotEmpty(message = "the national identity field is required.")
    @Size(min = 10, max = 10, message = "the national identity must be 10 digits.")
    @Pattern(message = "the national identity is invalid, it must start with 1 or 2.", regexp = "\\b[12]\\d{9}\\b")
    private String nationalIdentity;

    @NotEmpty(message = "the address field is required.")
    private String address;

    @NotEmpty(message = "the city field is required.")
    private String city;

    @NotEmpty(message = "the country field is required.")
    private String country;

    @NotNull(message = "the postal code field is required.")
    private Integer postalCode;

}
