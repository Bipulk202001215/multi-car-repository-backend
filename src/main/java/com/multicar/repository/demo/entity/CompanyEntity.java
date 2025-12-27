package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.generator.CompanyIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "CompanyEntity")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CompanyEntity {
    
    @Id
    @GeneratedValue(generator = "company-id-generator")
    @GenericGenerator(name = "company-id-generator", type = CompanyIdGenerator.class)
    @Column(name = "COMPID", length = 50)
    private String companyId;
    
    @Column(name = "company_name", nullable = false)
    private String companyName;
    
    @Column(name = "gstin", length = 15)
    private String gstin;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}

