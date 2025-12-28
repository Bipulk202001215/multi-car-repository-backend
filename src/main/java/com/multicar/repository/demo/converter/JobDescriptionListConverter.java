package com.multicar.repository.demo.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multicar.repository.demo.model.JobDescription;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class JobDescriptionListConverter implements AttributeConverter<List<JobDescription>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<JobDescription> jobDescriptions) {
        if (jobDescriptions == null || jobDescriptions.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(jobDescriptions);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize job descriptions to JSON", e);
        }
    }

    @Override
    public List<JobDescription> convertToEntityAttribute(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<JobDescription>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize job descriptions from JSON", e);
        }
    }
}

