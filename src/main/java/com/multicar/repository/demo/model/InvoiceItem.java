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
public class InvoiceItem {
    private String partCode;
    private Integer units;
    private String partDescription;
    private BigDecimal unitsPrice;
    private BigDecimal totalPrice;
    private BigDecimal discountedPrice;
}


