package com.example.hospital_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hospital_manager.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByFullNameContainingIgnoreCase(String name);
}