package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.generator.RolePermissionIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "RolePermissionEntity")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RolePermissionEntity {
    
    @Id
    @GeneratedValue(generator = "role-permission-id-generator")
    @GenericGenerator(name = "role-permission-id-generator", type = RolePermissionIdGenerator.class)
    @Column(name = "ROLEPERMISSIONID", length = 50)
    private String rolePermissionId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "ROLEID", nullable = false)
    private RoleEntity roleId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", referencedColumnName = "PERMID", nullable = false)
    private PermissionEntity permission;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}



