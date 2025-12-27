package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.enums.GstSlab;
import com.multicar.repository.demo.enums.InventoryCategory;
import com.multicar.repository.demo.generator.InventoryIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "INVENTORY")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class InventoryEntity {
    
    @Id
    @GeneratedValue(generator = "inventory-id-generator")
    @GenericGenerator(name = "inventory-id-generator", type = InventoryIdGenerator.class)
    @Column(name = "INVID", length = 50)
    private String inventoryId;
    
    @Column(name = "part_code", nullable = false)
    private String partCode;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "bar_code")
    private String barCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private InventoryCategory category;
    
    @Column(name = "hsn_code")
    private String hsnCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "gst_slab", nullable = false)
    private GstSlab gstSlab;
    
    @Column(name = "stock_qty")
    private Integer stockQty;
    
    @Column(name = "min_stock_alert")
    private Integer minStockAlert;
    
    @Column(name = "price_total_units", precision = 19, scale = 2)
    private BigDecimal priceTotalUnits;
    
    @Column(name = "total_units", nullable = false)
    private Integer totalUnits;
    
    @Column(name = "remaining_units", nullable = false)
    private Integer remainingUnits;
    
    @Column(name = "units_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal unitsPrice;
    
    @Column(name = "company_id", nullable = false)
    private String companyId;
    
    @Column(name = "supplier_id", nullable = false)
    private String supplierId;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}



