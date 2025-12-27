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
public class Supplier {
    private String supplierId;
    private String name;
    private String mobile;
    private String gstin;
    private String address;
    private String companyId;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}



