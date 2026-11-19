package com.example.hospital_manager.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.repository.AppointmentRepository;
import com.example.hospital_manager.repository.MedicalRecordRepository;
import com.example.hospital_manager.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public List<Patient> getAllPatients(String search) {
        if (search != null && !search.isBlank()) {
            return patientRepository.findByFullNameContainingIgnoreCase(search);
        }
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found"));
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient updated) {
        Patient existing = getPatientById(id);
        existing.setFullName(updated.getFullName());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setGender(updated.getGender());
        existing.setPhone(updated.getPhone());
        existing.setEmail(updated.getEmail());
        existing.setAddress(updated.getAddress());
        return patientRepository.save(existing);
    }

    public void deletePatient(Long id) {
        Patient existing = getPatientById(id);
        if (appointmentRepository.existsByPatientId(id) || medicalRecordRepository.existsByPatientId(id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Cannot delete patient with existing appointments or medical records");
        }
        patientRepository.delete(existing);
    }
}