package com.multicar.repository.demo.entity;

import com.multicar.repository.demo.enums.UserType;
import com.multicar.repository.demo.generator.UserIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserEntity", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email_id")
})
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserEntity {
    
    @Id
    @GeneratedValue(generator = "user-id-generator")
    @GenericGenerator(name = "user-id-generator", type = UserIdGenerator.class)
    @Column(name = "USID", length = 50)
    private String userId;
    
    @Column(name = "email_id", nullable = false, unique = true)
    private String emailId;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "COMPID")
    private CompanyEntity companyId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;
    
    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}



