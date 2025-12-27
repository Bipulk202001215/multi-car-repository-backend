package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.InventoryEntity;
import com.multicar.repository.demo.entity.SoldInventoryItemEntity;
import com.multicar.repository.demo.model.CreateInventoryRequest;
import com.multicar.repository.demo.model.CreateInvoiceRequest;
import com.multicar.repository.demo.model.Inventory;
import com.multicar.repository.demo.model.InventoryAlert;
import com.multicar.repository.demo.model.SellFromInventory;
import com.multicar.repository.demo.model.SellFromInventoryRequest;
import com.multicar.repository.demo.model.SoldInventoryItem;
import com.multicar.repository.demo.repository.InventoryRepository;
import com.multicar.repository.demo.repository.SoldInventoryItemRepository;
import com.multicar.repository.demo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final SoldInventoryItemRepository soldInventoryItemRepository;
    private final InvoiceService invoiceService;

    public Inventory createInventory(CreateInventoryRequest request) {
        InventoryEntity entity = InventoryEntity.builder()
                .partCode(request.getPartCode())
                .name(request.getName())
                .barCode(request.getBarCode())
                .category(request.getCategory())
                .hsnCode(request.getHsnCode())
                .gstSlab(request.getGstSlab())
                .stockQty(request.getStockQty())
                .minStockAlert(request.getMinStock())
                .priceTotalUnits(request.getPriceTotalUnits())
                .totalUnits(request.getTotalUnits())
                .remainingUnits(request.getTotalUnits()) // Initialize remaining_units = total_units
                .unitsPrice(request.getUnitsPrice())
                .companyId(request.getCompanyId())
                .supplierId(request.getSupplierId())
                .build();
        
        InventoryEntity savedEntity = inventoryRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public Optional<Inventory> getInventoryById(String inventoryId) {
        return inventoryRepository.findByInventoryId(inventoryId)
                .map(this::convertToModel);
    }

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<Inventory> updateInventory(String inventoryId, CreateInventoryRequest request) {
        return inventoryRepository.findByInventoryId(inventoryId)
                .map(existingEntity -> {
                    existingEntity.setPartCode(request.getPartCode());
                    existingEntity.setName(request.getName());
                    existingEntity.setBarCode(request.getBarCode());
                    existingEntity.setCategory(request.getCategory());
                    existingEntity.setHsnCode(request.getHsnCode());
                    existingEntity.setGstSlab(request.getGstSlab());
                    existingEntity.setStockQty(request.getStockQty());
                    existingEntity.setMinStockAlert(request.getMinStock());
                    existingEntity.setPriceTotalUnits(request.getPriceTotalUnits());
                    existingEntity.setUnitsPrice(request.getUnitsPrice());
                    existingEntity.setCompanyId(request.getCompanyId());
                    existingEntity.setSupplierId(request.getSupplierId());
                    
                    // If total_units is updated, adjust remaining_units proportionally
                    if (request.getTotalUnits() != null && !request.getTotalUnits().equals(existingEntity.getTotalUnits())) {
                        int oldTotal = existingEntity.getTotalUnits();
                        int newTotal = request.getTotalUnits();
                        int oldRemaining = existingEntity.getRemainingUnits();
                        // Calculate new remaining based on proportion
                        int newRemaining = (int) Math.round((double) oldRemaining * newTotal / oldTotal);
                        existingEntity.setTotalUnits(newTotal);
                        existingEntity.setRemainingUnits(Math.min(newRemaining, newTotal));
                    }
                    
                    InventoryEntity updatedEntity = inventoryRepository.save(existingEntity);
                    return convertToModel(updatedEntity);
                });
    }

    public boolean deleteInventory(String inventoryId) {
        Optional<InventoryEntity> entity = inventoryRepository.findByInventoryId(inventoryId);
        if (entity.isPresent()) {
            inventoryRepository.delete(entity.get());
            return true;
        }
        return false;
    }

    public List<SoldInventoryItem> sellFromInventory(SellFromInventoryRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("No items provided for sale");
        }
        
        // Step 1: Validate all items first - check if all requested_units <= remaining_units
        List<InventoryEntity> inventoriesToUpdate = new java.util.ArrayList<>();
        for (SellFromInventory item : request.getItems()) {
            InventoryEntity inventory = inventoryRepository.findByPartCode(item.getPartCode())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for part code: " + item.getPartCode()));
            
            // Validate: requested_units <= remaining_units
            if (item.getRequestedUnits() > inventory.getRemainingUnits()) {
                throw new RuntimeException("Insufficient stock for part code: " + item.getPartCode() 
                        + ". Requested: " + item.getRequestedUnits() 
                        + ", Available: " + inventory.getRemainingUnits());
            }
            
            inventoriesToUpdate.add(inventory);
        }
        
        // Step 2: If all validations pass, process all items in transaction
        List<SoldInventoryItem> soldItems = new java.util.ArrayList<>();
        BigDecimal totalSubtotal = BigDecimal.ZERO;
        String jobId = null;
        
        for (int i = 0; i < request.getItems().size(); i++) {
            SellFromInventory item = request.getItems().get(i);
            InventoryEntity inventory = inventoriesToUpdate.get(i);
            
            // Store jobId from first item (all items should have same jobId)
            if (jobId == null) {
                jobId = item.getJobId();
            }
            
            // Calculate prices
            BigDecimal unitsPrice = inventory.getUnitsPrice();
            BigDecimal discountRate = item.getDiscountRate() != null ? item.getDiscountRate() : BigDecimal.ZERO;
            BigDecimal discountMultiplier = BigDecimal.ONE.subtract(discountRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
            BigDecimal discountedPrice = unitsPrice.multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);
            BigDecimal sellPrice = discountedPrice.multiply(BigDecimal.valueOf(item.getRequestedUnits())).setScale(2, RoundingMode.HALF_UP);
            
            // Accumulate subtotal
            totalSubtotal = totalSubtotal.add(sellPrice);
            
            // Create SoldInventoryItem record
            SoldInventoryItemEntity soldItem = SoldInventoryItemEntity.builder()
                    .inventoryId(inventory)
                    .jobId(item.getJobId())
                    .unitsSold(item.getRequestedUnits())
                    .unitsPrice(unitsPrice)
                    .discountedPrice(discountedPrice)
                    .sellPrice(sellPrice)
                    .build();
            
            SoldInventoryItemEntity savedSoldItem = soldInventoryItemRepository.save(soldItem);
            
            // Update inventory.remaining_units = remaining_units - requested_units
            inventory.setRemainingUnits(inventory.getRemainingUnits() - item.getRequestedUnits());
            inventoryRepository.save(inventory);
            
            soldItems.add(convertSoldItemToModel(savedSoldItem));
        }
        
        // Step 3: Auto-create invoice if invoice details are provided
        if (jobId != null && (request.getCgst() != null || request.getSgst() != null || request.getIgst() != null || request.getPaymentMode() != null)) {
            CreateInvoiceRequest invoiceRequest = CreateInvoiceRequest.builder()
                    .jobId(jobId)
                    .subtotal(totalSubtotal)
                    .cgst(request.getCgst())
                    .sgst(request.getSgst())
                    .igst(request.getIgst())
                    .paymentMode(request.getPaymentMode())
                    .build();
            
            invoiceService.createInvoice(invoiceRequest);
        }
        
        return soldItems;
    }

    public List<InventoryAlert> getStockAlerts(String companyId) {
        List<InventoryEntity> lowStockItems;
        if (companyId != null && !companyId.isEmpty()) {
            lowStockItems = inventoryRepository.findLowStockItemsByCompanyId(companyId);
        } else {
            lowStockItems = inventoryRepository.findLowStockItems();
        }
        
        return lowStockItems.stream()
                .map(this::convertToAlert)
                .collect(Collectors.toList());
    }

    private Inventory convertToModel(InventoryEntity entity) {
        return Inventory.builder()
                .inventoryId(entity.getInventoryId())
                .partCode(entity.getPartCode())
                .name(entity.getName())
                .barCode(entity.getBarCode())
                .category(entity.getCategory())
                .hsnCode(entity.getHsnCode())
                .gstSlab(entity.getGstSlab())
                .stockQty(entity.getStockQty())
                .minStockAlert(entity.getMinStockAlert())
                .priceTotalUnits(entity.getPriceTotalUnits())
                .totalUnits(entity.getTotalUnits())
                .remainingUnits(entity.getRemainingUnits())
                .unitsPrice(entity.getUnitsPrice())
                .companyId(entity.getCompanyId())
                .supplierId(entity.getSupplierId())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }

    private InventoryAlert convertToAlert(InventoryEntity entity) {
        return InventoryAlert.builder()
                .inventoryId(entity.getInventoryId())
                .partCode(entity.getPartCode())
                .name(entity.getName())
                .minStockAlert(entity.getMinStockAlert())
                .remainingUnits(entity.getRemainingUnits())
                .stockQty(entity.getStockQty())
                .companyId(entity.getCompanyId())
                .supplierId(entity.getSupplierId())
                .unitsPrice(entity.getUnitsPrice())
                .build();
    }

    private SoldInventoryItem convertSoldItemToModel(SoldInventoryItemEntity entity) {
        return SoldInventoryItem.builder()
                .soldInventoryId(entity.getSoldInventoryId())
                .inventoryId(entity.getInventoryId() != null ? entity.getInventoryId().getInventoryId() : null)
                .jobId(entity.getJobId())
                .unitsSold(entity.getUnitsSold())
                .unitsPrice(entity.getUnitsPrice())
                .discountedPrice(entity.getDiscountedPrice())
                .sellPrice(entity.getSellPrice())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}

