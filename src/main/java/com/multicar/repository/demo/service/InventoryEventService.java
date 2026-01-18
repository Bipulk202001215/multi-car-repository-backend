package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.InventoryEventEntity;
import com.multicar.repository.demo.model.InventoryEventModel;
import com.multicar.repository.demo.repository.InventoryEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryEventService {

    private final InventoryEventRepository inventoryEventRepository;

    public InventoryEventModel createEvent(InventoryEventEntity entity) {
        InventoryEventEntity savedEntity = inventoryEventRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public List<InventoryEventModel> getEventsByPartCode(String partCode) {
        return inventoryEventRepository.findByPartCode(partCode).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public List<InventoryEventModel> getEventsByJobId(String jobId) {
        return inventoryEventRepository.findByJobId(jobId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public List<InventoryEventModel> getAllEvents() {
        return inventoryEventRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    private InventoryEventModel convertToModel(InventoryEventEntity entity) {
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
                .discount(entity.getDiscount())
                .build();
    }
}





