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
@Table(
        name = "employee",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_employee_email_branch", columnNames = {"branch_id", "email"})
        },
        indexes = {
                @Index(name = "idx_employee_branch", columnList = "branch_id")
        }
)
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchEntity branch;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 30)
    private String phone;

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] avatar;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_service",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"employee_id", "service_id"})
            },
            indexes = {
                    @Index(name = "idx_employee_service_employee", columnList = "employee_id"),
                    @Index(name = "idx_employee_service_service", columnList = "service_id")
            }
    )
    @Builder.Default
    private Set<ServiceEntity> services = new HashSet<>();
}