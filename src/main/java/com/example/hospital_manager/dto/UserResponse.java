package com.example.hospital_manager.dto;

import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Role;

public record UserResponse(
    Long id,
    String username,
    String fullName,
    Role role
) {
    public static UserResponse from(AppUser user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getFullName(), user.getRole());
    }
}