package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.enums.PaymentMode;
import com.multicar.repository.demo.enums.PaymentStatus;
import com.multicar.repository.demo.generator.InvoiceIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "INVOICES")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class InvoiceEntity {
    
    @Id
    @GeneratedValue(generator = "invoice-id-generator")
    @GenericGenerator(name = "invoice-id-generator", type = InvoiceIdGenerator.class)
    @Column(name = "INVID", length = 50)
    private String invoiceId;
    
    @Column(name = "job_id", nullable = false)
    private String jobId;
    
    @Column(name = "subtotal", precision = 19, scale = 2, nullable = false)
    private BigDecimal subtotal;
    
    @Column(name = "cgst", precision = 19, scale = 2)
    private BigDecimal cgst;
    
    @Column(name = "sgst", precision = 19, scale = 2)
    private BigDecimal sgst;
    
    @Column(name = "igst", precision = 19, scale = 2)
    private BigDecimal igst;
    
    @Column(name = "total", precision = 19, scale = 2, nullable = false)
    private BigDecimal total;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}





