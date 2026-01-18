package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.multicar.repository.demo.enums.JobStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobCard {
    private String jobCardId;
    private JobDetail jobDetailId;
    private String vehicleNumber;
    private String vehicleModel;
    private Integer kmReading;
    private LocalDateTime jobDate;
    private JobStatus status;
    private LocalDateTime checkinTime;
    private LocalDateTime estimatedDelivery;
    private String invoiceId;
    private String mobileNumber;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}



