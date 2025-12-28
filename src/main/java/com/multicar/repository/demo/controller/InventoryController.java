package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.exception.ErrorCode;
import com.multicar.repository.demo.exception.ResourceNotFoundException;
import com.multicar.repository.demo.model.CreateInventoryRequest;
import com.multicar.repository.demo.model.Inventory;
import com.multicar.repository.demo.model.InventoryAlert;
import com.multicar.repository.demo.model.SellFromInventoryRequest;
import com.multicar.repository.demo.model.SoldInventoryItem;
import com.multicar.repository.demo.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Controller 1: Create Inventory
    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody CreateInventoryRequest request) {
        Inventory createdInventory = inventoryService.createInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInventory);
    }

    // Controller 2: Sell From Inventory
    @PostMapping("/sell")
    public ResponseEntity<List<SoldInventoryItem>> sellFromInventory(@RequestBody SellFromInventoryRequest request) {
        List<SoldInventoryItem> soldItems = inventoryService.sellFromInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(soldItems);
    }

    // Controller 3: Get Stock Alerts
    @GetMapping("/alerts")
    public ResponseEntity<List<InventoryAlert>> getStockAlerts(@RequestParam(required = false) String companyId) {
        List<InventoryAlert> alerts = inventoryService.getStockAlerts(companyId);
        return ResponseEntity.ok(alerts);
    }

    // Additional CRUD endpoints
    @GetMapping("/{inventoryId}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable String inventoryId) {
        Inventory inventory = inventoryService.getInventoryById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId, ErrorCode.INVENTORY_NOT_FOUND));
        return ResponseEntity.ok(inventory);
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventories() {
        List<Inventory> inventories = inventoryService.getAllInventories();
        return ResponseEntity.ok(inventories);
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<Inventory> updateInventory(
            @PathVariable String inventoryId,
            @RequestBody CreateInventoryRequest request) {
        Inventory updatedInventory = inventoryService.updateInventory(inventoryId, request)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + inventoryId, ErrorCode.INVENTORY_NOT_FOUND));
        return ResponseEntity.ok(updatedInventory);
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable String inventoryId) {
        boolean deleted = inventoryService.deleteInventory(inventoryId);
        if (!deleted) {
            throw new ResourceNotFoundException("Inventory not found with id: " + inventoryId, ErrorCode.INVENTORY_NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }
}

