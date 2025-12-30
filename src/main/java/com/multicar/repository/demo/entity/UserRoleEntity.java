package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.generator.UserRoleIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserRoleEntity", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "role_id"})
})
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserRoleEntity {
    
    @Id
    @GeneratedValue(generator = "user-role-id-generator")
    @GenericGenerator(name = "user-role-id-generator", type = UserRoleIdGenerator.class)
    @Column(name = "USERROLEID", length = 50)
    private String userRoleId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "USID", nullable = false)
    private UserEntity userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "ROLEID", nullable = false)
    private RoleEntity roleId;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}





