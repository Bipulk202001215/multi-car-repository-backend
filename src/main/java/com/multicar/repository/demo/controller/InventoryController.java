package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.exception.ErrorCode;
import com.multicar.repository.demo.exception.ResourceNotFoundException;
import com.multicar.repository.demo.model.AddInventoryRequest;
import com.multicar.repository.demo.model.InventoryEventModel;
import com.multicar.repository.demo.model.Partcode;
import com.multicar.repository.demo.model.SellInventoryRequest;
import com.multicar.repository.demo.service.InventoryEventService;
import com.multicar.repository.demo.service.PartcodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final PartcodeService partcodeService;
    private final InventoryEventService inventoryEventService;

    // Add units to inventory
    @PostMapping("/add")
    public ResponseEntity<InventoryEventModel> addUnits(@RequestBody AddInventoryRequest request) {
        InventoryEventModel event = partcodeService.addUnits(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    // Sell units from inventory
    @PostMapping("/sell")
    public ResponseEntity<InventoryEventModel> sellUnits(@RequestBody SellInventoryRequest request) {
        InventoryEventModel event = partcodeService.sellUnits(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    // Get partcode by part code
    @GetMapping("/partcode/{partCode}")
    public ResponseEntity<Partcode> getPartcodeByPartCode(@PathVariable String partCode) {
        Partcode partcode = partcodeService.getPartcodeByPartCode(partCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Partcode not found with part code: " + partCode, 
                        ErrorCode.PARTCODE_NOT_FOUND));
        return ResponseEntity.ok(partcode);
    }

    // Get all partcodes
    @GetMapping("/partcode")
    public ResponseEntity<List<Partcode>> getAllPartcodes() {
        List<Partcode> partcodes = partcodeService.getAllPartcodes();
        return ResponseEntity.ok(partcodes);
    }

    // Get all events
    @GetMapping("/events")
    public ResponseEntity<List<InventoryEventModel>> getAllEvents() {
        List<InventoryEventModel> events = inventoryEventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // Get events by part code
    @GetMapping("/events/{partCode}")
    public ResponseEntity<List<InventoryEventModel>> getEventsByPartCode(@PathVariable String partCode) {
        List<InventoryEventModel> events = inventoryEventService.getEventsByPartCode(partCode);
        return ResponseEntity.ok(events);
    }

    // Get low stock alerts
    @GetMapping("/alerts")
    public ResponseEntity<List<Partcode>> getStockAlerts() {
        List<Partcode> alerts = partcodeService.getLowStockAlerts();
        return ResponseEntity.ok(alerts);
    }
}
