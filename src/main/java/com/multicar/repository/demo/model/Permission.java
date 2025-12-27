package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Permission {
    
    private String permissionId;
    private com.multicar.repository.demo.enums.Permission permissionName;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}

