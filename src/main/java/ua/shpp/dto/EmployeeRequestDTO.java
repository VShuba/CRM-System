package ua.shpp.dto;

import ua.shpp.entity.ServiceEntity;

import java.util.Set;

public record EmployeeRequestDTO(String firstName, String lastName, String email, String phone,
                                 Set<ServiceEntity> services) {

}