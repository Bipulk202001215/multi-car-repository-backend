package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryAlert {
    private String inventoryId;
    private String partCode;
    private String name;
    private Integer minStockAlert;
    private Integer remainingUnits;
    private Integer stockQty;
    private String companyId;
    private String supplierId;
    private BigDecimal unitsPrice;
}



