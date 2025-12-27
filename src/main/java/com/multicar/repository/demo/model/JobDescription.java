package com.multicar.repository.demo.model;

import com.multicar.repository.demo.enums.ServiceType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Embeddable
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class JobDescription {
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    private String description;
    private String assignedMechanicType;
    private String estimatedTime;
}

