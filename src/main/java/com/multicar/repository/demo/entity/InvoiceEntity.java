package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.converter.AdditionalInvoiceDetailsConverter;
import com.multicar.repository.demo.enums.PaymentMode;
import com.multicar.repository.demo.enums.PaymentStatus;
import com.multicar.repository.demo.generator.InvoiceIdGenerator;
import com.multicar.repository.demo.model.AdditionalInvoiceDetails;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

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
    
    @Column(name = "company_id", length = 50)
    private String companyId;
    
    @Column(name = "subtotal", precision = 19, scale = 2, nullable = false)
    private BigDecimal subtotal;
    
    @Column(name = "total", precision = 19, scale = 2, nullable = false)
    private BigDecimal total;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;
    
    @Convert(converter = AdditionalInvoiceDetailsConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "additional_details", columnDefinition = "jsonb", nullable = true)
    private AdditionalInvoiceDetails additionalDetails;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}







