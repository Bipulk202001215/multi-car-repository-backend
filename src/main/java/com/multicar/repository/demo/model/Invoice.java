package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.multicar.repository.demo.enums.PaymentMode;
import com.multicar.repository.demo.enums.PaymentStatus;
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
public class Invoice {
    private String invoiceId;
    private String jobId;
    private BigDecimal subtotal;
    private BigDecimal cgst;
    private BigDecimal sgst;
    private BigDecimal igst;
    private BigDecimal total;
    private PaymentStatus paymentStatus;
    private PaymentMode paymentMode;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}



