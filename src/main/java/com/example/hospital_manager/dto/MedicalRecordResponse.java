package com.example.hospital_manager.dto;

import java.time.LocalDateTime;

import com.example.hospital_manager.model.MedicalRecord;

public record MedicalRecordResponse(
    Long id,
    Long patientId,
    String patientName,
    Long doctorId,
    String doctorName,
    String diagnosis,
    String prescription,
    LocalDateTime createdAt
) {
    public static MedicalRecordResponse from(MedicalRecord r) {
        return new MedicalRecordResponse(
            r.getId(),
            r.getPatient().getId(),
            r.getPatient().getFullName(),
            r.getDoctor().getId(),
            r.getDoctor().getFullName(),
            r.getDiagnosis(),
            r.getPrescription(),
            r.getCreatedAt()
        );
    }
}