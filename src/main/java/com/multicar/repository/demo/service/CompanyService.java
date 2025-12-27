package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.CompanyEntity;
import com.multicar.repository.demo.model.Company;
import com.multicar.repository.demo.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company createCompany(Company company) {
        CompanyEntity entity = CompanyEntity.builder()
                .companyName(company.getCompanyName())
                .gstin(company.getGstIn())
                .build();
        
        CompanyEntity savedEntity = companyRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public Optional<Company> getCompanyById(String companyId) {
        return companyRepository.findByCompanyId(companyId)
                .map(this::convertToModel);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<Company> updateCompany(String companyId, Company company) {
        return companyRepository.findByCompanyId(companyId)
                .map(existingEntity -> {
                    existingEntity.setCompanyName(company.getCompanyName());
                    existingEntity.setGstin(company.getGstIn());
                    CompanyEntity updatedEntity = companyRepository.save(existingEntity);
                    return convertToModel(updatedEntity);
                });
    }

    public boolean deleteCompany(String companyId) {
        Optional<CompanyEntity> entity = companyRepository.findByCompanyId(companyId);
        if (entity.isPresent()) {
            companyRepository.delete(entity.get());
            return true;
        }
        return false;
    }

    public Optional<Company> getCompanyByGstin(String gstin) {
        return companyRepository.findByGstin(gstin)
                .map(this::convertToModel);
    }

    public boolean existsByGstin(String gstin) {
        return companyRepository.existsByGstin(gstin);
    }

    private Company convertToModel(CompanyEntity entity) {
        return Company.builder()
                .companyId(entity.getCompanyId())
                .companyName(entity.getCompanyName())
                .gstIn(entity.getGstin())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}

