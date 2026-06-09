package com.bullfitapp.bullfit.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 32, message = "Username must be 3-32 characters")
    private String username;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Min(value = 13, message = "Must be at least 13 years old")
    @Max(value = 110, message = "Cmon, you're not 110.")
    private Integer age;
}
