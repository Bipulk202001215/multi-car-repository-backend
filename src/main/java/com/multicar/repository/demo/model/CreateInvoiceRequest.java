package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.multicar.repository.demo.enums.PaymentMode;
import com.multicar.repository.demo.enums.PaymentStatus;
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
public class CreateInvoiceRequest {
    private String jobId;
    private BigDecimal subtotal;
    private BigDecimal cgst;
    private BigDecimal sgst;
    private BigDecimal igst;
    private PaymentStatus paymentStatus; // Optional, defaults to PENDING
    private PaymentMode paymentMode;
}



