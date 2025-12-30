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
public class Partcode {
    private String partCode;
    private BigDecimal unitsPrice;
    private Integer units;
    private Integer minStockAlert;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}


