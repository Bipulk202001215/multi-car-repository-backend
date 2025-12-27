package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.multicar.repository.demo.enums.UserType;
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
public class User {
    
    private String userId;
    private String emailId;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    private Company companyId;
    private UserType userType;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}

