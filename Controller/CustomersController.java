package com.example.capstone2updated.Controller;

import com.example.capstone2updated.DTO.CustomerDTO;
import com.example.capstone2updated.Model.Customer;
import com.example.capstone2updated.Model.User;
import com.example.capstone2updated.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomersController {

    private final CustomerService customerService;

    @GetMapping("/get")
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/search/id/{id}")
    public ResponseEntity<Customer> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.findById(id));
    }


    @GetMapping("/search/national-identity/{nationalIdentity}")
    public ResponseEntity<Customer> findByNationalIdentity(@PathVariable String nationalIdentity) {
        return ResponseEntity.ok(customerService.findByNationalId(nationalIdentity));
    }


    @GetMapping("/search/phone-number/{phoneNumber}")
    public ResponseEntity<Customer> findByPhoneNumber(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(customerService.findByPhoneNumber(phoneNumber));
    }

    @PostMapping("/create")
    public ResponseEntity<HashMap<String, Object>> createCustomer(@RequestBody @Valid CustomerDTO customerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customerDTO));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HashMap<String, Object>> updateCustomer(@PathVariable Integer id, @RequestBody @Valid CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
    }

    // this can only be performed by a user with ADMIN role.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HashMap<String, Object>> deleteCustomer(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(customerService.deleteCustomer(id, user));
    }

}
