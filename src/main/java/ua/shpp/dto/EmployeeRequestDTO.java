package ua.shpp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ua.shpp.entity.ServiceEntity;

import java.util.Set;

public record EmployeeRequestDTO(@JsonProperty("branch_id") Long branchId, @JsonProperty("first_name") String firstName,
                                 @JsonProperty("last_name") String lastName, String email, String phone,
                                 Set<ServiceEntity> services) {
}