package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.generator.SupplierIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "SUPPLIERS")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SupplierEntity {
    
    @Id
    @GeneratedValue(generator = "supplier-id-generator")
    @GenericGenerator(name = "supplier-id-generator", type = SupplierIdGenerator.class)
    @Column(name = "SUPID", length = 50)
    private String supplierId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "mobile", length = 15)
    private String mobile;
    
    @Column(name = "gstin", length = 15)
    private String gstin;
    
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "company_id", nullable = false)
    private String companyId;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}









