package ua.shpp.dto;

import ua.shpp.entity.ServiceEntity;

import java.util.Set;

public record EmployeeResponseDTO(String firstName, String lastName, String email, String phone, String base64Avatar,
                                  Set<ServiceEntity> services) {

}