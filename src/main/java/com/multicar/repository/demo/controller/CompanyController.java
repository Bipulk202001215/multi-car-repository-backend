package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.exception.ErrorCode;
import com.multicar.repository.demo.exception.ResourceNotFoundException;
import com.multicar.repository.demo.model.Company;
import com.multicar.repository.demo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company createdCompany = companyService.createCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String companyId) {
        Company company = companyService.getCompanyById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId, ErrorCode.COMPANY_NOT_FOUND));
        return ResponseEntity.ok(company);
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<Company> updateCompany(
            @PathVariable String companyId,
            @RequestBody Company company) {
        Company updatedCompany = companyService.updateCompany(companyId, company)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId, ErrorCode.COMPANY_NOT_FOUND));
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable String companyId) {
        boolean deleted = companyService.deleteCompany(companyId);
        if (!deleted) {
            throw new ResourceNotFoundException("Company not found with id: " + companyId, ErrorCode.COMPANY_NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/gstin/{gstin}")
    public ResponseEntity<Company> getCompanyByGstin(@PathVariable String gstin) {
        Company company = companyService.getCompanyByGstin(gstin)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with GSTIN: " + gstin, ErrorCode.COMPANY_NOT_FOUND));
        return ResponseEntity.ok(company);
    }

    @GetMapping("/exists/gstin/{gstin}")
    public ResponseEntity<Boolean> checkGstinExists(@PathVariable String gstin) {
        boolean exists = companyService.existsByGstin(gstin);
        return ResponseEntity.ok(exists);
    }
}



