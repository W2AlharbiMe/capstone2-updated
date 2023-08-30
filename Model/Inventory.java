package com.example.capstone2updated.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "the max capacity field is required.")
    @Positive(message = "the max capacity must be positive number.")
    @Column(nullable = false)
    private Integer maxCapacity; // how many cars can you store in this inventory ?

    @NotEmpty(message = "the country field is required.")
    @Column(nullable = false)
    private String country;

    @NotEmpty(message = "the city field is required.")
    @Column(nullable = false)
    private String city;

    @NotEmpty(message = "the address field is required.")
    @Column(nullable = false)
    private String address;


    // Relations

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventory")
    private Set<InventoryItem> inventoryItems;

}
