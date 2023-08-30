package com.example.capstone2updated.Controller;

import com.example.capstone2updated.DTO.DealerServiceDTO;
import com.example.capstone2updated.Model.DealerService;
import com.example.capstone2updated.Service.DealerServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dealer-services")
@RequiredArgsConstructor
public class DealerServiceController {

    private final DealerServiceService dealerServiceService;

    @GetMapping("/get")
    public ResponseEntity<List<DealerService>> findAll() {
        return ResponseEntity.ok(dealerServiceService.findAll());
    }

    @GetMapping("/search/id/{id}")
    public ResponseEntity<DealerService> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(dealerServiceService.findById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<HashMap<String, Object>> addPart(@RequestBody @Valid DealerServiceDTO dealerServiceDTO) {
        return ResponseEntity.ok(dealerServiceService.addService(dealerServiceDTO));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HashMap<String, Object>> updateService(@PathVariable Integer id, @RequestBody @Valid DealerServiceDTO dealerServiceDTO) {
        return ResponseEntity.ok(dealerServiceService.updateService(id, dealerServiceDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HashMap<String, Object>> deleteService(@PathVariable Integer id) {
        return ResponseEntity.ok(dealerServiceService.deleteService(id));
    }

}
