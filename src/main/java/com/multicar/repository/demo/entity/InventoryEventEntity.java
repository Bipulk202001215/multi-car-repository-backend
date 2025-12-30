package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.enums.InventoryEvent;
import com.multicar.repository.demo.generator.InventoryEventIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "INVENTORY_EVENT")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class InventoryEventEntity {
    
    @Id
    @GeneratedValue(generator = "inventory-event-id-generator")
    @GenericGenerator(name = "inventory-event-id-generator", type = InventoryEventIdGenerator.class)
    @Column(name = "event_id", length = 50)
    private String eventId;
    
    @Column(name = "part_code", nullable = false, length = 100)
    private String partCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "event", nullable = false)
    private InventoryEvent event;
    
    @Column(name = "units", nullable = false)
    private Integer units;
    
    @Column(name = "price", precision = 19, scale = 2, nullable = false)
    private BigDecimal price;
    
    @Column(name = "units_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal unitsPrice;
    
    @Column(name = "company_id", length = 50)
    private String companyId;
    
    @Column(name = "job_id", length = 50)
    private String jobId;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
}


