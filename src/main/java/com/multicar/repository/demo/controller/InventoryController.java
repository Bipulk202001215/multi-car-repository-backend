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
    @PostMapping(value = "/{companyId}/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<InventoryEventModel> addUnits(@PathVariable String companyId, @RequestBody AddInventoryRequest request) {
        InventoryEventModel event = partcodeService.addUnits(companyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    // Sell units from inventory
    @PostMapping("/{companyId}/sell")
    public ResponseEntity<InventoryEventModel> sellUnits(@PathVariable String companyId, @RequestBody SellInventoryRequest request) {
        InventoryEventModel event = partcodeService.sellUnits(companyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    // Get partcode by part code
    @GetMapping("/{companyId}/partcode/{partCode}")
    public ResponseEntity<Partcode> getPartcodeByPartCode(@PathVariable String companyId, @PathVariable String partCode) {
        Partcode partcode = partcodeService.getPartcodeByPartCode(companyId, partCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Partcode not found with part code: " + partCode, 
                        ErrorCode.PARTCODE_NOT_FOUND));
        return ResponseEntity.ok(partcode);
    }

    // Get all partcodes
    @GetMapping("/{companyId}/partcode")
    public ResponseEntity<List<Partcode>> getAllPartcodes(@PathVariable String companyId) {
        List<Partcode> partcodes = partcodeService.getAllPartcodes(companyId);
        return ResponseEntity.ok(partcodes);
    }

    // Get all events
    @GetMapping("/{companyId}/events")
    public ResponseEntity<List<InventoryEventModel>> getAllEvents(@PathVariable String companyId) {
        List<InventoryEventModel> events = inventoryEventService.getAllEvents(companyId);
        return ResponseEntity.ok(events);
    }

    // Get events by part code
    @GetMapping("/{companyId}/events/{partCode}")
    public ResponseEntity<List<InventoryEventModel>> getEventsByPartCode(@PathVariable String companyId, @PathVariable String partCode) {
        List<InventoryEventModel> events = inventoryEventService.getEventsByPartCode(companyId, partCode);
        return ResponseEntity.ok(events);
    }

    // Get low stock alerts
    @GetMapping("/{companyId}/alerts")
    public ResponseEntity<List<Partcode>> getStockAlerts(@PathVariable String companyId) {
        List<Partcode> alerts = partcodeService.getLowStockAlerts(companyId);
        return ResponseEntity.ok(alerts);
    }
}
