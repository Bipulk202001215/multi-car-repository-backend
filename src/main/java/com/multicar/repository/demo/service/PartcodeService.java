package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.InventoryEventEntity;
import com.multicar.repository.demo.entity.PartcodeEntity;
import com.multicar.repository.demo.enums.InventoryEvent;
import com.multicar.repository.demo.exception.BusinessException;
import com.multicar.repository.demo.exception.ErrorCode;
import com.multicar.repository.demo.exception.ResourceNotFoundException;
import com.multicar.repository.demo.model.AddInventoryRequest;
import com.multicar.repository.demo.model.InventoryEventModel;
import com.multicar.repository.demo.model.Partcode;
import com.multicar.repository.demo.model.SellInventoryRequest;
import com.multicar.repository.demo.repository.InventoryEventRepository;
import com.multicar.repository.demo.repository.PartcodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PartcodeService {

    private final PartcodeRepository partcodeRepository;
    private final InventoryEventRepository inventoryEventRepository;

    public InventoryEventModel addUnits(AddInventoryRequest request) {
        // Validation: units is mandatory
        if (request.getUnits() == null) {
            throw new BusinessException("Units field is required", ErrorCode.UNITS_REQUIRED);
        }
        
        // Validation: units_price is mandatory
        if (request.getUnitsPrice() == null) {
            throw new BusinessException("Units price field is required", ErrorCode.UNITS_PRICE_REQUIRED);
        }
        
        // Validation: units must be positive
        if (request.getUnits() <= 0) {
            throw new BusinessException("Units must be greater than 0", ErrorCode.VALIDATION_ERROR);
        }
        
        // Validation: units_price must be positive
        if (request.getUnitsPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Units price must be greater than 0", ErrorCode.VALIDATION_ERROR);
        }
        
        // Find or create PartcodeEntity
        PartcodeEntity partcode = partcodeRepository.findByPartCode(request.getPartCode())
                .orElseGet(() -> {
                    // Create new partcode if it doesn't exist
                    return PartcodeEntity.builder()
                            .partCode(request.getPartCode())
                            .unitsPrice(request.getUnitsPrice())
                            .part_desc(request.getPartDesc())
                            .part_company(request.getPartCompany())
                            .units(request.getUnits())
                            .supplierId(request.getSupplierId())
                            .minStockAlert(2) // Default value
                            .build();
                });
        
        // Calculate price
        BigDecimal price = request.getUnitsPrice().multiply(BigDecimal.valueOf(request.getUnits()));
        
        // Create ADD event
        InventoryEventEntity eventEntity = InventoryEventEntity.builder()
                .partCode(request.getPartCode())
                .event(InventoryEvent.ADD)
                .units(request.getUnits())
                .unitsPrice(request.getUnitsPrice())
                .price(price)
                .companyId(null) // Optional for ADD
                .jobId(null) // Optional for ADD
                .build();
        
        InventoryEventEntity savedEvent = inventoryEventRepository.save(eventEntity);
        
        // Update partcode: increase units and update units_price
        partcode.setUnits(partcode.getUnits() + request.getUnits());
        partcode.setUnitsPrice(request.getUnitsPrice()); // Store units_price from event
        partcodeRepository.save(partcode);
        
        return convertEventToModel(savedEvent);
    }

    public InventoryEventModel sellUnits(SellInventoryRequest request) {
        // Validation: units is mandatory
        if (request.getUnits() == null) {
            throw new BusinessException("Units field is required", ErrorCode.UNITS_REQUIRED);
        }
        
        // Validation: partcode is mandatory
        if (request.getPartCode() == null || request.getPartCode().trim().isEmpty()) {
            throw new BusinessException("Part code field is required", ErrorCode.PARTCODE_REQUIRED);
        }
        
        // Validation: units must be positive
        if (request.getUnits() <= 0) {
            throw new BusinessException("Units must be greater than 0", ErrorCode.VALIDATION_ERROR);
        }
        
        // Find PartcodeEntity
        PartcodeEntity partcode = partcodeRepository.findByPartCode(request.getPartCode())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Partcode not found with part code: " + request.getPartCode(), 
                        ErrorCode.PARTCODE_NOT_FOUND));
        
        // Validation: sufficient stock
        if (request.getUnits() > partcode.getUnits()) {
            throw new BusinessException(
                    "Insufficient stock for part code: " + request.getPartCode() 
                    + ". Requested: " + request.getUnits() 
                    + ", Available: " + partcode.getUnits(),
                    ErrorCode.INSUFFICIENT_STOCK);
        }
        
        // Use units_price from partcode
        BigDecimal unitsPrice = partcode.getUnitsPrice();
        BigDecimal price = unitsPrice.multiply(BigDecimal.valueOf(request.getUnits()));
        
        // Create SELL event
        InventoryEventEntity eventEntity = InventoryEventEntity.builder()
                .partCode(request.getPartCode())
                .event(InventoryEvent.SELL)
                .units(request.getUnits())
                .unitsPrice(unitsPrice) // Maintain from partcode
                .price(price)
                .discount(request.getDiscount())
                .companyId(request.getCompanyId())
                .jobId(request.getJobId())
                .build();
        
        InventoryEventEntity savedEvent = inventoryEventRepository.save(eventEntity);
        
        // Update partcode: decrease units (units_price remains unchanged)
        partcode.setUnits(partcode.getUnits() - request.getUnits());
        partcodeRepository.save(partcode);
        
        return convertEventToModel(savedEvent);
    }

    public Optional<Partcode> getPartcodeByPartCode(String partCode) {
        return partcodeRepository.findByPartCode(partCode)
                .map(this::convertToModel);
    }

    public List<Partcode> getAllPartcodes() {
        return partcodeRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public List<Partcode> getLowStockAlerts() {
        return partcodeRepository.findLowStockItems().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    private Partcode convertToModel(PartcodeEntity entity) {
        return Partcode.builder()
                .partCode(entity.getPartCode())
                .unitsPrice(entity.getUnitsPrice())
                .units(entity.getUnits())
                .minStockAlert(entity.getMinStockAlert())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }

    private InventoryEventModel convertEventToModel(InventoryEventEntity entity) {
        return InventoryEventModel.builder()
                .eventId(entity.getEventId())
                .partCode(entity.getPartCode())
                .event(entity.getEvent())
                .units(entity.getUnits())
                .price(entity.getPrice())
                .unitsPrice(entity.getUnitsPrice())
                .companyId(entity.getCompanyId())
                .jobId(entity.getJobId())
                .createdOn(entity.getCreatedOn())
                .build();
    }
}




