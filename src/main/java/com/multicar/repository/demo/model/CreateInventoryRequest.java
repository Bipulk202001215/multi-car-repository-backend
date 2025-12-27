package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.multicar.repository.demo.enums.GstSlab;
import com.multicar.repository.demo.enums.InventoryCategory;
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
public class CreateInventoryRequest {
    private String partCode;
    private String name;
    private String barCode;
    private InventoryCategory category;
    private String hsnCode;
    private GstSlab gstSlab;
    private Integer stockQty;
    private Integer minStock;
    private BigDecimal priceTotalUnits;
    private Integer totalUnits;
    private BigDecimal unitsPrice;
    private String companyId;
    private String supplierId;
}



