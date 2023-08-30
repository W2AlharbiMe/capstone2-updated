package com.example.capstone2updated.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "the name field is required.")
    @Size(min = 3, message = "the name must be at least 3 chars.")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "the establishment year field is required.")
    @Min(message = "invalid establishment year it must be 4 digits and a valid year.", value = 1900) // this will add SQL check constraint.
    @Column(nullable = false)
    private Integer  establishmentYear;


    // relations

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "manufacturer")
    private Set<Car> cars;

}
