package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.exception.ErrorCode;
import com.multicar.repository.demo.exception.ResourceNotFoundException;
import com.multicar.repository.demo.model.Supplier;
import com.multicar.repository.demo.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
        Supplier createdSupplier = supplierService.createSupplier(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupplier);
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable String supplierId) {
        Supplier supplier = supplierService.getSupplierById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + supplierId, ErrorCode.SUPPLIER_NOT_FOUND));
        return ResponseEntity.ok(supplier);
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<Supplier> updateSupplier(
            @PathVariable String supplierId,
            @RequestBody Supplier supplier) {
        Supplier updatedSupplier = supplierService.updateSupplier(supplierId, supplier)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + supplierId, ErrorCode.SUPPLIER_NOT_FOUND));
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable String supplierId) {
        boolean deleted = supplierService.deleteSupplier(supplierId);
        if (!deleted) {
            throw new ResourceNotFoundException("Supplier not found with id: " + supplierId, ErrorCode.SUPPLIER_NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Supplier>> getSuppliersByCompanyId(@PathVariable String companyId) {
        List<Supplier> suppliers = supplierService.getSuppliersByCompanyId(companyId);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/gstin/{gstin}")
    public ResponseEntity<Supplier> getSupplierByGstin(@PathVariable String gstin) {
        Supplier supplier = supplierService.getSupplierByGstin(gstin)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with GSTIN: " + gstin, ErrorCode.SUPPLIER_NOT_FOUND));
        return ResponseEntity.ok(supplier);
    }
}



