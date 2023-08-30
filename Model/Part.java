package com.example.capstone2updated.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "parts")
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private Double purchasePrice;


    @Column(columnDefinition = "bit(1) default 0")
    private Boolean isUsed = false;

    // Relation

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "part")
    @JsonIgnore
    private Set<InventoryItem> inventoryItems;

}
