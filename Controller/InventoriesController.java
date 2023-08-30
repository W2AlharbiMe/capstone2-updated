package com.example.capstone2updated.Controller;

import com.example.capstone2updated.DTO.InventoryDTO;
import com.example.capstone2updated.Model.Inventory;
import com.example.capstone2updated.Service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoriesController {

    private final InventoryService inventoryService;


    @GetMapping("/get")
    public ResponseEntity<List<Inventory>> findAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }


    @GetMapping("/search/id/{id}")
    public ResponseEntity<Inventory> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(inventoryService.findById(id));
    }


    @PostMapping("/add")
    public ResponseEntity<HashMap<String, Object>> addInventory(@RequestBody @Valid InventoryDTO inventoryDTO) {
        return ResponseEntity.ok(inventoryService.addInventory(inventoryDTO));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HashMap<String, Object>> updateInventory(@PathVariable Integer id, @RequestBody @Valid InventoryDTO inventoryDTO) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryDTO));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HashMap<String, Object>> deleteInventory(@PathVariable Integer id) {
        return ResponseEntity.ok(inventoryService.deleteInventory(id));
    }

}
