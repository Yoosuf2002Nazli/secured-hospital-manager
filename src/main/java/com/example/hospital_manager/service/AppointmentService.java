package com.example.hospital_manager.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.hospital_manager.dto.AppointmentRequest;
import com.example.hospital_manager.dto.AppointmentResponse;
import com.example.hospital_manager.dto.StatusRequest;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Appointment;
import com.example.hospital_manager.model.AppointmentStatus;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.repository.AppointmentRepository;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public AppointmentResponse bookAppointment(AppointmentRequest req) {
        Patient patient = patientRepository.findById(req.patientId())
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found"));
        AppUser doctor = userRepository.findById(req.doctorId())
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Doctor not found"));

        if (doctor.getRole() != Role.DOCTOR) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Assigned user is not a DOCTOR");
        }

        Appointment appointment = Appointment.builder()
            .patient(patient)
            .doctor(doctor)
            .appointmentDateTime(req.appointmentDateTime())
            .reason(req.reason())
            .status(AppointmentStatus.SCHEDULED)
            .build();

        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> getAppointmentsForUser(String username) {
        AppUser currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        List<Appointment> list = (currentUser.getRole() == Role.DOCTOR)
            ? appointmentRepository.findByDoctorId(currentUser.getId())
            : appointmentRepository.findAll();

        return list.stream().map(AppointmentResponse::from).toList();
    }

    public AppointmentResponse updateStatus(Long id, StatusRequest req, String username) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Appointment not found"));
        AppUser currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        // Ownership rule: doctors can only modify their own appointments
        if (currentUser.getRole() == Role.DOCTOR && !appointment.getDoctor().getId().equals(currentUser.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Doctors can only update their own appointments");
        }

        appointment.setStatus(req.status());
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }
}