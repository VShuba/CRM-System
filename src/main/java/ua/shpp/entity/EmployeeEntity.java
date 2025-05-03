package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
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
                @UniqueConstraint(name = "uq_employee_branch_id_and_email", columnNames = {"branch_id", "email"})
        },
        indexes = {@Index(name = "idx_employee_branch", columnList = "branch_id")}
)
public class EmployeeEntity {
    @Id
    @Column(columnDefinition = "smallint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false, columnDefinition = "smallint")
    private BranchEntity branch;

    @Column(nullable = false, length = 70)
    private String name;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(length = 15)
    private String phone;

    @Lob
    @Column(nullable = false)
    private byte[] avatar;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_service",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"),
            uniqueConstraints = {
                    @UniqueConstraint(name = "uq_employee_service_employee_id_and_service_id",
                            columnNames = {"employee_id", "service_id"})
            },
            indexes = {
                    @Index(name = "idx_employee_service_employee", columnList = "employee_id"),
                    @Index(name = "idx_employee_service_service", columnList = "service_id")
            }
    )
    @Builder.Default
    private Set<ServiceEntity> services = new HashSet<>();

    @OneToMany(mappedBy = "employee",fetch = FetchType.LAZY, orphanRemoval = true)
    @Column(name = "schedule_event_id")
    private List<ScheduleEventEntity> scheduleEventEntities;

    @PreRemove
    private void preRemove() {
        for (ScheduleEventEntity scheduleEvent : this.scheduleEventEntities) {
            scheduleEvent.setEmployee(null);
        }

        for (ServiceEntity service : this.services) {
            service.getEmployees().remove(this);
        }
    }
}