package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.enums.JobStatus;
import com.multicar.repository.demo.generator.JobCardIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "JOB_CARDS")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class JobCardEntity {
    
    @Id
    @GeneratedValue(generator = "job-card-id-generator")
    @GenericGenerator(name = "job-card-id-generator", type = JobCardIdGenerator.class)
    @Column(name = "JBCDId", length = 50)
    private String jobCardId;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "job_detail_id", referencedColumnName = "JBID", nullable = false)
    private JobDetailEntity jobDetailId;
    
    @Column(name = "vehicle_number", nullable = false)
    private String vehicleNumber;

    @Column(name = "vehicle_number", nullable = false)
    private String mobileNumber;
    
    @Column(name = "km_reading")
    private Integer kmReading;
    
    @Column(name = "job_date")
    private LocalDateTime jobDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status;
    
    @Column(name = "checkin_time")
    private LocalDateTime checkinTime;
    
    @Column(name = "estimated_delivery")
    private LocalDateTime estimatedDelivery;
    
    @Column(name = "invoice_id", nullable = true)
    private String invoiceId;

    @Column(name = "company_id", nullable = false)
    private String companyId;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}



