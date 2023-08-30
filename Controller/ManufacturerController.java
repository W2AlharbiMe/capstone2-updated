package com.example.capstone2updated.Controller;

import com.example.capstone2updated.DTO.ManufacturerDTO;
import com.example.capstone2updated.Model.Manufacturer;
import com.example.capstone2updated.Service.ManufacturerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @GetMapping("/get")
    public ResponseEntity<List<Manufacturer>> findAll() {
        return ResponseEntity.ok(manufacturerService.findAll());
    }

    @GetMapping("/search/id/{id}")
    public ResponseEntity<Manufacturer> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(manufacturerService.findById(id));
    }

    // /api/v1/manufacturers/search?name=abcd
    @GetMapping("/search")
    public ResponseEntity<List<Manufacturer>> searchByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(manufacturerService.searchByName(name));
    }

    @PostMapping("/add")
    public ResponseEntity<HashMap<String, Object>> addManufacturer(@RequestBody @Valid ManufacturerDTO manufacturerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(manufacturerService.addManufacturer(manufacturerDTO));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HashMap<String, Object>> updateManufacturer(@PathVariable Integer id, @RequestBody @Valid ManufacturerDTO manufacturerDTO) {
        return ResponseEntity.ok(manufacturerService.updateManufacturer(id, manufacturerDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HashMap<String, Object>> deleteManufacturer(@PathVariable Integer id) {
        return ResponseEntity.ok(manufacturerService.deleteManufacturer(id));
    }
}
