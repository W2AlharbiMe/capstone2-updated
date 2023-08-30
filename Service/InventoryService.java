package com.example.capstone2updated.Service;

import com.example.capstone2updated.Api.Exception.ResourceNotFoundException;
import com.example.capstone2updated.Api.Exception.SimpleException;
import com.example.capstone2updated.DTO.InventoryDTO;
import com.example.capstone2updated.Model.Inventory;
import com.example.capstone2updated.Repository.InventoryItemRepository;
import com.example.capstone2updated.Repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryItemRepository inventoryItemRepository;



    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public Inventory findById(Integer id) throws ResourceNotFoundException {
        Inventory inventory = inventoryRepository.findInventoryById(id);

        if(inventory == null) {
            throw new ResourceNotFoundException("inventory");
        }

        return inventory;
    }

    public HashMap<String, Object> addInventory(InventoryDTO inventoryDTO) {

        Inventory inventory = new Inventory();

        inventory.setAddress(inventoryDTO.getAddress());
        inventory.setCity(inventoryDTO.getCity());
        inventory.setCountry(inventoryDTO.getCountry());
        inventory.setMaxCapacity(inventoryDTO.getMaxCapacity());

        inventory.setInventoryItems(Set.of()); // Set.of() is used to create empty Set

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the inventory have been added.");
        response.put("inventory", inventoryRepository.save(inventory));

        return response;
    }

    public HashMap<String, Object> updateInventory(Integer id, InventoryDTO inventoryDTO) throws ResourceNotFoundException {
        Inventory inventory = inventoryRepository.findInventoryById(id);

        if(inventory == null) {
            throw new ResourceNotFoundException("inventory");
        }

        inventory.setAddress(inventoryDTO.getAddress());
        inventory.setCity(inventoryDTO.getCity());
        inventory.setCountry(inventoryDTO.getCountry());
        inventory.setMaxCapacity(inventoryDTO.getMaxCapacity());

        inventoryRepository.save(inventory);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the inventory have been updated.");
        response.put("inventory", inventory);

        return response;
    }


    public HashMap<String, Object> deleteInventory(Integer id) throws ResourceNotFoundException, SimpleException {
        Inventory inventory = inventoryRepository.findInventoryById(id);

        if(inventory == null) {
            throw new ResourceNotFoundException("inventory");
        }

        if(inventoryItemRepository.atLeastOneItem(id) != null) {
            throw new SimpleException("you can not delete this inventory because there are items that are associated with this inventory id.");
        }

        inventoryRepository.deleteById(id);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the inventory have been deleted.");
        response.put("inventory", inventory);

        return response;
    }

}
