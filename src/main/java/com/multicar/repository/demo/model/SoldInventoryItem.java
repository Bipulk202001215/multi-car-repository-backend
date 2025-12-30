package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class SoldInventoryItem {
    private String soldInventoryId;
    private String inventoryId;
    private String jobId;
    private Integer unitsSold;
    private BigDecimal unitsPrice;
    private BigDecimal discountedPrice;
    private BigDecimal sellPrice;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}





