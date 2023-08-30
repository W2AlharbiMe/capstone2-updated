package com.example.capstone2updated.Controller;

import com.example.capstone2updated.DTO.InventoryItemDTO;
import com.example.capstone2updated.Model.InventoryItem;
import com.example.capstone2updated.Service.InventoryItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory-items")
@RequiredArgsConstructor
public class InventoryItemsController {

    private final InventoryItemService inventoryItemService;

    @GetMapping("/get/{inventoryId}")
    private ResponseEntity<List<InventoryItem>> findAllByInventoryId(@PathVariable Integer inventoryId) {
        return ResponseEntity.ok(inventoryItemService.findAllItemsInInventory(inventoryId));
    }

    @GetMapping("/search/id/{id}")
    private ResponseEntity<InventoryItem> findOneById(@PathVariable Integer id) {
        return ResponseEntity.ok(inventoryItemService.findById(id));
    }

    @GetMapping("/search/car-id/{carId}")
    private ResponseEntity<List<InventoryItem>> findAllByCarId(@PathVariable Integer carId) {
        return ResponseEntity.ok(inventoryItemService.findAllByCarId(carId));
    }

    @GetMapping("/search/part-id/{partId}")
    private ResponseEntity<List<InventoryItem>> findAllByPartId(@PathVariable Integer partId) {
        return ResponseEntity.ok(inventoryItemService.findAllByPartId(partId));
    }

    @GetMapping("/search/{type}")
    private ResponseEntity<List<InventoryItem>> findOneByItemId(@PathVariable String type) {
        return ResponseEntity.ok(inventoryItemService.findByType(type));
    }



    @PostMapping("/add")
    private ResponseEntity<HashMap<String, Object>> addItem(@RequestBody @Valid InventoryItemDTO inventoryItemDTO) {
        return ResponseEntity.ok(inventoryItemService.addInventoryItem(inventoryItemDTO));
    }


    @PutMapping("/update/{id}")
    private ResponseEntity<HashMap<String, Object>> updateItem(@PathVariable Integer id, @RequestBody @Valid InventoryItemDTO inventoryItemDTO) {
        return ResponseEntity.ok(inventoryItemService.updateInventoryItem(id, inventoryItemDTO));
    }


    @DeleteMapping("/delete/{id}")
    private ResponseEntity<HashMap<String, Object>> deleteItem(@PathVariable Integer id) {
        return ResponseEntity.ok(inventoryItemService.deleteInventoryItem(id));
    }

}
