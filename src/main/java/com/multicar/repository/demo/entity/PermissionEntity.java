package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.enums.Permission;
import com.multicar.repository.demo.generator.PermissionIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "PermissionEntity")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PermissionEntity {
    
    @Id
    @GeneratedValue(generator = "permission-id-generator")
    @GenericGenerator(name = "permission-id-generator", type = PermissionIdGenerator.class)
    @Column(name = "PERMID", length = 50)
    private String permissionId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "permission_name", nullable = false, unique = true)
    private Permission permissionName;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}



