package com.darumng.ems.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
    private Long managerId;
    private String managerName;
    private Integer employeeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
