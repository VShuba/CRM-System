package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "employee", uniqueConstraints = {
        @UniqueConstraint(name = "unique_employee_email_organization", columnNames = {"branch_id", "email"})
})
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    Organization organization;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private BranchEntity branch;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] avatar;

    @ManyToMany
    @JoinTable(
            name = "employee_service",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @Builder.Default
    private Set<ServiceEntity> services = new HashSet<>();
}