package com.example.hospital_manager.dto;

import java.time.LocalDateTime;

import com.example.hospital_manager.model.Appointment;
import com.example.hospital_manager.model.AppointmentStatus;

public record AppointmentResponse(
    Long id,
    Long patientId,
    String patientName,
    Long doctorId,
    String doctorName,
    LocalDateTime appointmentDateTime,
    String reason,
    AppointmentStatus status
) {
    public static AppointmentResponse from(Appointment a) {
        return new AppointmentResponse(
            a.getId(),
            a.getPatient().getId(),
            a.getPatient().getFullName(),
            a.getDoctor().getId(),
            a.getDoctor().getFullName(),
            a.getAppointmentDateTime(),
            a.getReason(),
            a.getStatus()
        );
    }
}