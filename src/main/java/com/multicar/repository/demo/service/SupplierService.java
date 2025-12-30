package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.SupplierEntity;
import com.multicar.repository.demo.model.Supplier;
import com.multicar.repository.demo.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public Supplier createSupplier(Supplier supplier) {
        SupplierEntity entity = SupplierEntity.builder()
                .name(supplier.getName())
                .mobile(supplier.getMobile())
                .gstin(supplier.getGstin())
                .address(supplier.getAddress())
                .companyId(supplier.getCompanyId())
                .build();
        
        SupplierEntity savedEntity = supplierRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public Optional<Supplier> getSupplierById(String supplierId) {
        return supplierRepository.findBySupplierId(supplierId)
                .map(this::convertToModel);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<Supplier> updateSupplier(String supplierId, Supplier supplier) {
        return supplierRepository.findBySupplierId(supplierId)
                .map(existingEntity -> {
                    existingEntity.setName(supplier.getName());
                    existingEntity.setMobile(supplier.getMobile());
                    existingEntity.setGstin(supplier.getGstin());
                    existingEntity.setAddress(supplier.getAddress());
                    existingEntity.setCompanyId(supplier.getCompanyId());
                    SupplierEntity updatedEntity = supplierRepository.save(existingEntity);
                    return convertToModel(updatedEntity);
                });
    }

    public boolean deleteSupplier(String supplierId) {
        Optional<SupplierEntity> entity = supplierRepository.findBySupplierId(supplierId);
        if (entity.isPresent()) {
            supplierRepository.delete(entity.get());
            return true;
        }
        return false;
    }

    public List<Supplier> getSuppliersByCompanyId(String companyId) {
        return supplierRepository.findByCompanyId(companyId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<Supplier> getSupplierByGstin(String gstin) {
        return supplierRepository.findByGstin(gstin)
                .map(this::convertToModel);
    }

    private Supplier convertToModel(SupplierEntity entity) {
        return Supplier.builder()
                .supplierId(entity.getSupplierId())
                .name(entity.getName())
                .mobile(entity.getMobile())
                .gstin(entity.getGstin())
                .address(entity.getAddress())
                .companyId(entity.getCompanyId())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}





