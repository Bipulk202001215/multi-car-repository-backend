package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.enums.UserRole;
import com.multicar.repository.demo.generator.RoleIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "RoleEntity")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RoleEntity {
    
    @Id
    @GeneratedValue(generator = "role-id-generator")
    @GenericGenerator(name = "role-id-generator", type = RoleIdGenerator.class)
    @Column(name = "ROLEID", length = 50)
    private String roleId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, unique = true)
    private UserRole roleName;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}





