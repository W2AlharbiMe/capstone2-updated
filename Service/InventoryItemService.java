package com.example.capstone2updated.Service;

import com.example.capstone2updated.Api.Exception.ResourceNotFoundException;
import com.example.capstone2updated.Api.Exception.SimpleException;
import com.example.capstone2updated.DTO.InventoryItemDTO;
import com.example.capstone2updated.Model.*;
import com.example.capstone2updated.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InventoryItemService {

    private final InventoryRepository inventoryRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final CarRepository carRepository;
    private final PartRepository partRepository;


    public List<InventoryItem> findAllItemsInInventory(Integer inventoryId) throws SimpleException {
        List<InventoryItem> inventoryItems = inventoryItemRepository.findInventoryItemsByInventoryId(inventoryId);

        if (inventoryItems.isEmpty()) {
            throw new SimpleException("this inventory is new it does not have any items.");
        }

        return inventoryItems;
    }


    public InventoryItem findById(Integer id) {
        InventoryItem inventoryItem = inventoryItemRepository.findInventoryItemById(id);

        if (inventoryItem == null) {
            throw new ResourceNotFoundException("inventory item");
        }

        return inventoryItem;
    }

    public List<InventoryItem> findAllByCarId(Integer carId) {
        List<InventoryItem> inventoryItems = inventoryItemRepository.findInventoryItemsByCarId(carId);

        if (inventoryItems.isEmpty()) {
            throw new ResourceNotFoundException("inventory item");
        }

        return inventoryItems;
    }

    public List<InventoryItem> findAllByPartId(Integer partId) {
        List<InventoryItem> inventoryItems = inventoryItemRepository.findInventoryItemsByPartId(partId);

        if (inventoryItems.isEmpty()) {
            throw new ResourceNotFoundException("inventory item");
        }

        return inventoryItems;
    }

    public List<InventoryItem> findByType(String type) {
        if (!(type.equalsIgnoreCase("car") || type.equalsIgnoreCase("part"))) {
            throw new SimpleException("invalid type it can only be ('car', 'part').");
        }

        List<InventoryItem> inventoryItem = inventoryItemRepository.findInventoryItemsByType(type);

        if (inventoryItem == null) {
            throw new ResourceNotFoundException("inventory item");
        }

        return inventoryItem;
    }

    public HashMap<String, Object> addInventoryItem(InventoryItemDTO inventoryItemDTO) {

        // make sure that there's either a carId or partId
        if (inventoryItemDTO.getCarId() == null && inventoryItemDTO.getPartId() == null) {
            throw new SimpleException("you need to specify either a car id or a part id.");
        }

        if ((inventoryItemDTO.getCarId() != null && inventoryItemDTO.getCarId() > 0) && (inventoryItemDTO.getPartId() != null && inventoryItemDTO.getPartId() > 0)) {
            throw new SimpleException("you can only store either a car or a part per inventory item.");
        }


        if (inventoryItemDTO.getCarId() != null) {
            validate(inventoryItemDTO, true, true);
        }

        if (inventoryItemDTO.getPartId() != null) {
            validate(inventoryItemDTO, false, true);
        }


        InventoryItem inventoryItem = new InventoryItem();

        inventoryItem.setInventory(inventoryRepository.findInventoryById(inventoryItemDTO.getInventoryId()));
        inventoryItem.setType(inventoryItemDTO.getType().toLowerCase());
        inventoryItem.setQuantity(inventoryItemDTO.getQuantity());

        if(inventoryItemDTO.getType().equalsIgnoreCase("car")) {
            inventoryItem.setCar(carRepository.findCarById(inventoryItemDTO.getCarId()));
        } else {
            inventoryItem.setPart(partRepository.findPartById(inventoryItemDTO.getPartId()));
        }


        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the inventory item have been added.");
        response.put("inventoryItem", inventoryItemRepository.save(inventoryItem));

        return response;
    }

    public HashMap<String, Object> updateInventoryItem(Integer id, InventoryItemDTO inventoryItemDTO) {

        InventoryItem inventoryItem = inventoryItemRepository.findInventoryItemById(id);

        if(inventoryItem == null) {
            throw new ResourceNotFoundException("inventory item");
        }

        // make sure that there's either a carId or partId
        if (inventoryItemDTO.getCarId() == null && inventoryItemDTO.getPartId() == null) {
            throw new SimpleException("you need to specify either a car id or a part id.");
        }

        if ((inventoryItemDTO.getCarId() != null && inventoryItemDTO.getCarId() > 0) && (inventoryItemDTO.getPartId() != null && inventoryItemDTO.getPartId() > 0)) {
            throw new SimpleException("you can only store either a car or a part per inventory item.");
        }

        if (inventoryItemDTO.getCarId() != null) {
            validate(inventoryItemDTO, true, false);
        }

        if (inventoryItemDTO.getPartId() != null) {
            validate(inventoryItemDTO, false, false);
        }


        inventoryItem.setInventory(inventoryRepository.findInventoryById(inventoryItemDTO.getInventoryId()));
        inventoryItem.setType(inventoryItemDTO.getType().toLowerCase());
        inventoryItem.setQuantity(inventoryItemDTO.getQuantity());

        if(inventoryItemDTO.getType().equalsIgnoreCase("car")) {
            inventoryItem.setCar(carRepository.findCarById(inventoryItemDTO.getCarId()));
        } else {
            inventoryItem.setPart(partRepository.findPartById(inventoryItemDTO.getPartId()));
        }


        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the inventory item have been added.");
        response.put("inventoryItem", inventoryItemRepository.save(inventoryItem));

        return response;
    }



    // 1. inventory exists
    // 2. inventory is full
    // 3. make sure not to allow user to insert a quantity that's greater than inventory max capacity
    // 4. make sure that the provided car or part isn't already stored in the specified inventory.
    private void validate(InventoryItemDTO inventoryItemDTO, boolean carCheck, boolean isStored) throws ResourceNotFoundException, SimpleException {
        Inventory inventory = inventoryRepository.findInventoryById(inventoryItemDTO.getInventoryId());

        // inventory must exist
        if (inventory == null) {
            throw new ResourceNotFoundException("inventory");
        }

        // check capacity
        if (inventoryItemDTO.getQuantity() > inventory.getMaxCapacity()) {
            throw new SimpleException("you are trying to store (" + inventoryItemDTO.getQuantity() + ") in inventory with max capacity of (" + inventory.getMaxCapacity() + ").");
        }

        Integer totalStoredItems = inventoryItemRepository.totalItemsInInventory(inventory.getId());

        if(totalStoredItems != null) {
            if (Objects.equals(totalStoredItems, inventory.getMaxCapacity()) || totalStoredItems >= inventory.getMaxCapacity()) {
                throw new SimpleException("this inventory is full.");
            }
        }


        if(carCheck) {
            // make sure that each inventory is storing 1 unique car.
            // one car per inventory.

            Car car = carRepository.findCarById(inventoryItemDTO.getCarId());

            if(car == null) {
                throw new ResourceNotFoundException("car");
            }

            if(isStored) {
                InventoryItem inventoryItems = inventoryItemRepository.atLeastOneCarPerInventory(inventory.getId(), inventoryItemDTO.getCarId());

                if (inventoryItems != null) {
                    throw new SimpleException("this car is already stored in this inventory. you can update the quantity.");
                }
            }

        } else {
            // check part

            Part part = partRepository.findPartById(inventoryItemDTO.getPartId());

            if(part == null) {
                throw new ResourceNotFoundException("part");
            }

            if(isStored) {
                // make sure that each inventory is storing 1 unique part.
                // one part per inventory.
                InventoryItem inventoryItems = inventoryItemRepository.atLeastOnePartPerInventory(inventory.getId(), inventoryItemDTO.getPartId());

                if (inventoryItems != null) {
                    throw new SimpleException("this part is already stored in this inventory. you can update the quantity.");
                }
            }

        }

    }

    public HashMap<String, Object> deleteInventoryItem(Integer id) {
        InventoryItem inventoryItem = inventoryItemRepository.findInventoryItemById(id);

        if(inventoryItem == null) {
            throw new ResourceNotFoundException("inventory item");
        }

        inventoryItemRepository.deleteById(id);

        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "the inventory item have been deleted.");
        response.put("inventoryItem", inventoryItem);

        return response;
    }

}
