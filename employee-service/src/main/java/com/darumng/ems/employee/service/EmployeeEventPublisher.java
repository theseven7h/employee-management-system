// ============= KAFKA EVENT PUBLISHER =============
package com.darumng.ems.employee.service;

import com.darumng.ems.employee.entity.Employee;
import com.darumng.ems.employee.entity.Department;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.employee-events}")
    private String employeeEventsTopic;

    @Value("${kafka.topic.department-events}")
    private String departmentEventsTopic;

    public void publishEmployeeCreated(Employee employee) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "EMPLOYEE_CREATED");
            event.put("employeeId", employee.getEmployeeId());
            event.put("email", employee.getEmail());
            event.put("firstName", employee.getFirstName());
            event.put("lastName", employee.getLastName());
            event.put("status", employee.getStatus());
            event.put("timestamp", System.currentTimeMillis());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(employeeEventsTopic, employee.getEmployeeId().toString(), message);

            log.info("Published EMPLOYEE_CREATED event for: {}", employee.getEmail());
        } catch (Exception e) {
            log.error("Failed to publish EMPLOYEE_CREATED event", e);
        }
    }

    public void publishEmployeeUpdated(Employee employee) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "EMPLOYEE_UPDATED");
            event.put("employeeId", employee.getEmployeeId());
            event.put("email", employee.getEmail());
            event.put("firstName", employee.getFirstName());
            event.put("lastName", employee.getLastName());
            event.put("status", employee.getStatus());
            event.put("timestamp", System.currentTimeMillis());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(employeeEventsTopic, employee.getEmployeeId().toString(), message);

            log.info("Published EMPLOYEE_UPDATED event for: {}", employee.getEmail());
        } catch (Exception e) {
            log.error("Failed to publish EMPLOYEE_UPDATED event", e);
        }
    }

    public void publishEmployeeDeleted(Long employeeId) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "EMPLOYEE_DELETED");
            event.put("employeeId", employeeId);
            event.put("timestamp", System.currentTimeMillis());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(employeeEventsTopic, employeeId.toString(), message);

            log.info("Published EMPLOYEE_DELETED event for ID: {}", employeeId);
        } catch (Exception e) {
            log.error("Failed to publish EMPLOYEE_DELETED event", e);
        }
    }

    public void publishDepartmentCreated(Department department) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "DEPARTMENT_CREATED");
            event.put("departmentId", department.getId());
            event.put("name", department.getName());
            event.put("description", department.getDescription());
            event.put("timestamp", System.currentTimeMillis());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(departmentEventsTopic, department.getId().toString(), message);

            log.info("Published DEPARTMENT_CREATED event for: {}", department.getName());
        } catch (Exception e) {
            log.error("Failed to publish DEPARTMENT_CREATED event", e);
        }
    }

    public void publishDepartmentUpdated(Department department) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "DEPARTMENT_UPDATED");
            event.put("departmentId", department.getId());
            event.put("name", department.getName());
            event.put("description", department.getDescription());
            event.put("timestamp", System.currentTimeMillis());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(departmentEventsTopic, department.getId().toString(), message);

            log.info("Published DEPARTMENT_UPDATED event for: {}", department.getName());
        } catch (Exception e) {
            log.error("Failed to publish DEPARTMENT_UPDATED event", e);
        }
    }

    public void publishDepartmentDeleted(Long departmentId) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "DEPARTMENT_DELETED");
            event.put("departmentId", departmentId);
            event.put("timestamp", System.currentTimeMillis());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(departmentEventsTopic, departmentId.toString(), message);

            log.info("Published DEPARTMENT_DELETED event for ID: {}", departmentId);
        } catch (Exception e) {
            log.error("Failed to publish DEPARTMENT_DELETED event", e);
        }
    }
}