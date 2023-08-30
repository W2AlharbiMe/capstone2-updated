package com.example.capstone2updated.DTO;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder(toBuilder = true) // this is used to take default initialized values into account.
public class SalesInvoiceDTO {

    @NotEmpty(message = "the type field is required.")
    @Pattern(message = "the type can only be `full_car_payment`, or `instalment_car_payment`, `full_service_payment`, ``, `instalment_service_payment`.", regexp = "(?i)\\b(full_car_payment|instalment_car_payment|full_service_payment|instalment_service_payment)\\b?")
    private String type;

    @NotEmpty(message = "the status field is required.")
    @Pattern(message = "the status can only be `pending`, or `paid`.", regexp = "(?i)\\b(pending|paid)\\b?")
    private String status;

    @PositiveOrZero(message = "the instalment per month can only be a 0 or positive number.")
    private Double instalmentPerMonth; // when type is full_payment then default this to 0

    @Positive(message = "the car id must be positive number.")
    private Integer carId;

    @Positive(message = "the service id must be positive number.")
    private Integer serviceId;

    @Positive(message = "the inventory item id must be positive number.")
    private Integer inventoryItemId;

    @NotNull(message = "the customer id field is required.")
    @Positive(message = "the customer id must be positive number.")
    private Integer customerId;

    private Double vat;

    @NotNull(message = "the sub price field is required.")
    @Positive(message = "the sub price must be positive number.")
    private Double subPrice; // car price without vat
}
