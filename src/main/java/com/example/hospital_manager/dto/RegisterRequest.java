package com.example.hospital_manager.dto;

import com.example.hospital_manager.model.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Username is required") String username,
    @NotBlank(message = "Password is required") @Size(min = 4, message = "Password must be at least 4 characters") String password,
    @NotBlank(message = "Full name is required") String fullName,
    @NotNull(message = "Role is required") Role role
) {}