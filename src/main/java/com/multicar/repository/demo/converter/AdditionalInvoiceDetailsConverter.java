package com.multicar.repository.demo.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multicar.repository.demo.model.AdditionalInvoiceDetails;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AdditionalInvoiceDetailsConverter implements AttributeConverter<AdditionalInvoiceDetails, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AdditionalInvoiceDetails additionalDetails) {
        if (additionalDetails == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(additionalDetails);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize additional invoice details to JSON", e);
        }
    }

    @Override
    public AdditionalInvoiceDetails convertToEntityAttribute(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, AdditionalInvoiceDetails.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize additional invoice details from JSON", e);
        }
    }
}
