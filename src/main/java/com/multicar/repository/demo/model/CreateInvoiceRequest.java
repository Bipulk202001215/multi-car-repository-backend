package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.multicar.repository.demo.enums.PaymentMode;
import com.multicar.repository.demo.enums.PaymentStatus;
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
public class CreateInvoiceRequest {
    private String jobId;
    private String companyId;
    private PaymentStatus paymentStatus; // Optional, defaults to PENDING
    private PaymentMode paymentMode;
    private List<InvoiceItem> items; // List of partCode and units for SELL events
}







