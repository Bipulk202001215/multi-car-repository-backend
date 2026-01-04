package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.multicar.repository.demo.enums.InventoryEvent;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryEventModel {
    private String eventId;
    private String partCode;
    private InventoryEvent event;
    private Integer units;
    private BigDecimal price;
    private BigDecimal unitsPrice;
    private String companyId;
    private String jobId;
    private LocalDateTime createdOn;
}



