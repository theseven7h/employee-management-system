package com.darumng.ems.employee.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private Long departmentId;

    @NotNull(message = "Status is required")
    private String status; // ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
}

