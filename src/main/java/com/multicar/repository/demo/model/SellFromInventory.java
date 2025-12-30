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
public class SellFromInventory {
    private Integer requestedUnits;
    private String partCode;
    private String jobId;
    private BigDecimal discountRate; // Percentage as decimal (e.g., 10.0 for 10%)
}





