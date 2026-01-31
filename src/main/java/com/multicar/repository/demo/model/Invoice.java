package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.multicar.repository.demo.enums.PaymentMode;
import com.multicar.repository.demo.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice {
    private String invoiceId;
    private String jobId;
    private String companyId;
    private BigDecimal subtotal;
    private BigDecimal cgst;
    private BigDecimal sgst;
    private BigDecimal igst;
    private BigDecimal total;
    private PaymentStatus paymentStatus;
    private PaymentMode paymentMode;
    private BigDecimal netCalculationAmount;
    private List<InvoiceItem> items; // Items (partCode and units) from SELL events
    private AdditionalInvoiceDetails additionalDetails; // Optional additional invoice details
    private String vehicleNumber; // Customer vehicle number from job card
    private String mobileNumber; // Customer mobile number from job card
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}







