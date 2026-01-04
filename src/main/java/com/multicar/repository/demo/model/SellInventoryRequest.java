package com.multicar.repository.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellInventoryRequest {
    private String partCode; // MANDATORY
    private Integer units; // MANDATORY
    private String companyId; // Required for SELL
    private String jobId; // Required for SELL
}



