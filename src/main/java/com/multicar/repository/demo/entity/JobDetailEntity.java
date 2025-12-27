package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.generator.JobDetailIdGenerator;
import com.multicar.repository.demo.model.JobDescription;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "JOB_DETAILS")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class JobDetailEntity {
    
    @Id
    @GeneratedValue(generator = "job-detail-id-generator")
    @GenericGenerator(name = "job-detail-id-generator", type = JobDetailIdGenerator.class)
    @Column(name = "JBID", length = 50)
    private String jobDetailId;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "serviceType", column = @Column(name = "service_type")),
        @AttributeOverride(name = "description", column = @Column(name = "description", columnDefinition = "TEXT")),
        @AttributeOverride(name = "assignedMechanicType", column = @Column(name = "assigned_mechanic_type")),
        @AttributeOverride(name = "estimatedTime", column = @Column(name = "estimated_time"))
    })
    private JobDescription jobDescription;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}



