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
@Table(name = "employee")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "employee_branch",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id"),
            uniqueConstraints = {
                    @UniqueConstraint(name = "uniqueEmployeeAndBranchIds", columnNames = {"employee_id", "branch_id"})
            }
    )
    @Builder.Default
    private Set<BranchEntity> branches = new HashSet<>();

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