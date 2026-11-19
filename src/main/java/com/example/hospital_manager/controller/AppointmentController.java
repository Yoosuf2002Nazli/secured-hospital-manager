package com.example.hospital_manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hospital_manager.dto.AppointmentRequest;
import com.example.hospital_manager.dto.AppointmentResponse;
import com.example.hospital_manager.dto.StatusRequest;
import com.example.hospital_manager.service.AppointmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<AppointmentResponse> book(@Valid @RequestBody AppointmentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.bookAppointment(req));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentResponse>> list(Authentication auth) {
        return ResponseEntity.ok(appointmentService.getAppointmentsForUser(auth.getName()));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusRequest req,
            Authentication auth) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, req, auth.getName()));
    }
}