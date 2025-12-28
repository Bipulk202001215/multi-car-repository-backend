package com.multicar.repository.demo.model;

import com.multicar.repository.demo.enums.ServiceType;
import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class JobDescription {
    private ServiceType serviceType;
    private String description;
    private String assignedMechanicType;
    private String estimatedTime;
}

