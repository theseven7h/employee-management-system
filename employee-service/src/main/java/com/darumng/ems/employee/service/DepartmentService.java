package com.darumng.ems.employee.service;

import com.darumng.ems.employee.dto.DepartmentRequest;
import com.darumng.ems.employee.dto.DepartmentResponse;
import com.darumng.ems.employee.entity.Department;
import com.darumng.ems.employee.entity.Employee;
import com.darumng.ems.employee.repository.DepartmentRepository;
import com.darumng.ems.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeEventPublisher eventPublisher;

    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Department with this name already exists");
        }

        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .managerId(request.getManagerId())
                .build();

        department = departmentRepository.save(department);
        log.info("Department created: {}", department.getName());

        eventPublisher.publishDepartmentCreated(department);

        return mapToResponse(department);
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return mapToResponse(department);
    }

    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        if (!department.getName().equals(request.getName()) &&
                departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Department name already exists");
        }

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setManagerId(request.getManagerId());

        department = departmentRepository.save(department);
        log.info("Department updated: {}", department.getName());

        eventPublisher.publishDepartmentUpdated(department);

        return mapToResponse(department);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        List<Employee> employees = employeeRepository.findByDepartmentId(id);
        if (!employees.isEmpty()) {
            throw new RuntimeException("Cannot delete department with existing employees");
        }

        departmentRepository.delete(department);
        log.info("Department deleted: {}", department.getName());

        eventPublisher.publishDepartmentDeleted(id);
    }

    private DepartmentResponse mapToResponse(Department department) {
        int employeeCount = employeeRepository.findByDepartmentId(department.getId()).size();

        String managerName = null;
        if (department.getManagerId() != null) {
            employeeRepository.findById(department.getManagerId())
                    .ifPresent(emp -> {
                    });
        }

        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .managerId(department.getManagerId())
                .managerName(managerName)
                .employeeCount(employeeCount)
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}
