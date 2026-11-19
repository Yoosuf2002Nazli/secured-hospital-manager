package com.example.hospital_manager.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hospital_manager.dto.AuthResponse;
import com.example.hospital_manager.dto.LoginRequest;
import com.example.hospital_manager.dto.RegisterRequest;
import com.example.hospital_manager.dto.UserResponse;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.repository.UserRepository;
import com.example.hospital_manager.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new ApiException(HttpStatus.CONFLICT, "Username already exists");
        }
        AppUser user = AppUser.builder()
            .username(req.username())
            .password(passwordEncoder.encode(req.password()))
            .fullName(req.fullName())
            .role(req.role())
            .build();
        return UserResponse.from(userRepository.save(user));
    }

    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );
        AppUser user = userRepository.findByUsername(req.username())
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "Bearer", jwtService.getExpirationMs(), user.getUsername(), user.getFullName(), user.getRole());
    }

    public UserResponse getCurrentUser(String username) {
        AppUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        return UserResponse.from(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    public List<UserResponse> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR).stream().map(UserResponse::from).toList();
    }
}