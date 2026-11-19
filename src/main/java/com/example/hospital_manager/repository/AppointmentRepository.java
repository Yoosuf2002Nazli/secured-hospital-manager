package com.example.hospital_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hospital_manager.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    boolean existsByPatientId(Long patientId);
}