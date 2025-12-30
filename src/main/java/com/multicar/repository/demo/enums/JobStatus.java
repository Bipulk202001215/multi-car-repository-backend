package com.multicar.repository.demo.enums;

public enum JobStatus {
    PENDING("Job is pending and not yet started"),
    IN_PROGRESS("Job is currently in progress"),
    QC("Job is in quality check phase"),
    READY("Job is completed and ready for delivery");

    private final String description;

    JobStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}





