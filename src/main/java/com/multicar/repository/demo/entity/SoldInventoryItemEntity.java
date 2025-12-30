package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.generator.SoldInventoryItemIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SOLD_INVENTORY_ITEMS")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SoldInventoryItemEntity {
    
    @Id
    @GeneratedValue(generator = "sold-inventory-item-id-generator")
    @GenericGenerator(name = "sold-inventory-item-id-generator", type = SoldInventoryItemIdGenerator.class)
    @Column(name = "SOLDINVID", length = 50)
    private String soldInventoryId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", referencedColumnName = "INVID", nullable = false)
    private InventoryEntity inventoryId;
    
    @Column(name = "job_id", nullable = false)
    private String jobId;
    
    @Column(name = "units_sold", nullable = false)
    private Integer unitsSold;
    
    @Column(name = "units_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal unitsPrice;
    
    @Column(name = "discounted_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal discountedPrice;
    
    @Column(name = "sell_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal sellPrice;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}





