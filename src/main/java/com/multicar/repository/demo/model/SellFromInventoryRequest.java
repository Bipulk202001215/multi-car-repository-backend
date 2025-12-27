package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.multicar.repository.demo.enums.PaymentMode;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellFromInventoryRequest {
    private List<SellFromInventory> items;
    // Invoice details (optional - if provided, invoice will be auto-created)
    private BigDecimal cgst;
    private BigDecimal sgst;
    private BigDecimal igst;
    private PaymentMode paymentMode;
}

