package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.SoldInventoryItemEntity;
import com.multicar.repository.demo.model.SoldInventoryItem;
import com.multicar.repository.demo.repository.SoldInventoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SoldInventoryItemService {

    private final SoldInventoryItemRepository soldInventoryItemRepository;

    public SoldInventoryItem createSoldItem(SoldInventoryItemEntity entity) {
        SoldInventoryItemEntity savedEntity = soldInventoryItemRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public List<SoldInventoryItem> getSoldItemsByInventoryId(String inventoryId) {
        return soldInventoryItemRepository.findByInventoryId_InventoryId(inventoryId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public List<SoldInventoryItem> getSoldItemsByJobId(String jobId) {
        return soldInventoryItemRepository.findByJobId(jobId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    private SoldInventoryItem convertToModel(SoldInventoryItemEntity entity) {
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

