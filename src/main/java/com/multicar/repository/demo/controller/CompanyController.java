package com.multicar.repository.demo.controller;

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
        return companyService.getCompanyById(companyId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
        return companyService.updateCompany(companyId, company)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable String companyId) {
        boolean deleted = companyService.deleteCompany(companyId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/gstin/{gstin}")
    public ResponseEntity<Company> getCompanyByGstin(@PathVariable String gstin) {
        return companyService.getCompanyByGstin(gstin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/exists/gstin/{gstin}")
    public ResponseEntity<Boolean> checkGstinExists(@PathVariable String gstin) {
        boolean exists = companyService.existsByGstin(gstin);
        return ResponseEntity.ok(exists);
    }
}



