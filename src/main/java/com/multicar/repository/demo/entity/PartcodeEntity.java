package com.multicar.repository.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PARTCODE")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PartcodeEntity {
    
    @Id
    @Column(name = "part_code", length = 100, nullable = false)
    private String partCode;

    @Column(name = "part_desc", length = 100, nullable = true)
    private String part_desc;

    @Column(name = "part_company" , length = 50, nullable = true)
    private String part_company;

    @Column(name = "units_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal unitsPrice;
    
    @Column(name = "units", nullable = false)
    private Integer units;

    @Column(name = "supplier_id", length = 50, nullable = true)
    private String supplierId;
    
    @Column(name = "company_id", length = 50, nullable = true)
    private String companyId;
    
    @Column(name = "min_stock_alert", columnDefinition = "INTEGER DEFAULT 2")
    private Integer minStockAlert;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}




