package com.example.capstone2updated.Repository;

import com.example.capstone2updated.Model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {
    InventoryItem findInventoryItemById(Integer id);

    List<InventoryItem> findInventoryItemsByInventoryId(Integer inventoryId);

    List<InventoryItem> findInventoryItemsByCarId(Integer id);


    List<InventoryItem> findInventoryItemsByPartId(Integer id);

    List<InventoryItem> findInventoryItemsByType(String type);


//    InventoryItem findByItemId(Integer id);


//    InventoryItem findByItemIdAndType(Integer id, String type);

    @Query("SELECT i FROM inventory_items i WHERE i.inventory.id = ?1 ORDER BY i.id ASC LIMIT 1")
    InventoryItem atLeastOneItem(Integer inventoryId);

    @Query("SELECT i FROM inventory_items i WHERE i.inventory.id = ?1 AND i.car.id = ?2 ORDER BY i.id ASC LIMIT 1")
    InventoryItem atLeastOneCarPerInventory(Integer inventoryId, Integer carId);

    @Query("SELECT i FROM inventory_items i WHERE i.inventory.id = ?1 AND i.part.id = ?2 ORDER BY i.id ASC LIMIT 1")
    InventoryItem atLeastOnePartPerInventory(Integer inventoryId, Integer partId);


    @Query("SELECT SUM(i.quantity) FROM inventory_items i WHERE i.inventory.id = ?1")
    Integer totalItemsInInventory(Integer inventoryId);
}
