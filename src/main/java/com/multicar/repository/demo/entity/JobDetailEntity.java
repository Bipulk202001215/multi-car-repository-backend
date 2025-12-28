package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.converter.JobDescriptionListConverter;
import com.multicar.repository.demo.generator.JobDetailIdGenerator;
import com.multicar.repository.demo.model.JobDescription;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

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
    
    @Convert(converter = JobDescriptionListConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "job_description", columnDefinition = "jsonb", nullable = true)
    private List<JobDescription> jobDescription;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}



