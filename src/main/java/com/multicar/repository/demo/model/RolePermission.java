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
public class RolePermission {
    
    private String rolePermissionId;
    private Role roleId;
    private Permission permission;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}



