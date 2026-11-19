package com.example.hospital_manager.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(AppUser.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .fullName("System Administrator")
                .role(Role.ADMIN)
                .build());

            userRepository.save(AppUser.builder()
                .username("doctor1")
                .password(passwordEncoder.encode("doctor123"))
                .fullName("Dr. Nimal Perera")
                .role(Role.DOCTOR)
                .build());

            userRepository.save(AppUser.builder()
                .username("nurse1")
                .password(passwordEncoder.encode("nurse123"))
                .fullName("Nurse Kasuni Silva")
                .role(Role.NURSE)
                .build());

            userRepository.save(AppUser.builder()
                .username("reception1")
                .password(passwordEncoder.encode("reception123"))
                .fullName("Kamal Fernando")
                .role(Role.RECEPTIONIST)
                .build());
        }

        if (patientRepository.count() == 0) {
            patientRepository.save(Patient.builder()
                .fullName("Saman Kumara")
                .dateOfBirth(LocalDate.of(1990, 5, 14))
                .gender("Male")
                .phone("0771234567")
                .email("saman@example.com")
                .address("123 Kandy Road, Kelaniya")
                .build());

            patientRepository.save(Patient.builder()
                .fullName("Dilini Jayawardena")
                .dateOfBirth(LocalDate.of(1995, 11, 22))
                .gender("Female")
                .phone("0719876543")
                .email("dilini@example.com")
                .address("45 Galle Road, Colombo")
                .build());
        }
    }
}