package com.example.hospital_manager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.hospital_manager.dto.MedicalRecordRequest;
import com.example.hospital_manager.dto.MedicalRecordResponse;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.MedicalRecord;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.repository.MedicalRecordRepository;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public MedicalRecordResponse createRecord(MedicalRecordRequest req, String doctorUsername) {
        Patient patient = patientRepository.findById(req.patientId())
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found"));
        AppUser doctor = userRepository.findByUsername(doctorUsername)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Doctor not found"));

        MedicalRecord record = MedicalRecord.builder()
            .patient(patient)
            .doctor(doctor)
            .diagnosis(req.diagnosis())
            .prescription(req.prescription())
            .createdAt(LocalDateTime.now())
            .build();

        return MedicalRecordResponse.from(medicalRecordRepository.save(record));
    }

    public List<MedicalRecordResponse> getRecordsByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Patient not found");
        }
        return medicalRecordRepository.findByPatientId(patientId)
            .stream().map(MedicalRecordResponse::from).toList();
    }
}