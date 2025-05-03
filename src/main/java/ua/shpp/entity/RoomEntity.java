package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"services", "branch"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "rooms",
        indexes = {
                @Index(name = "idx_room_branch", columnList = "branch_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_branch_room_name", columnNames = {"branch_id", "name"})
        }
)
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Builder.Default
    @ManyToMany(mappedBy = "rooms", fetch = FetchType.LAZY)
    private Set<ServiceEntity> services = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchEntity branch;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, orphanRemoval = true)
    @Column(name = "schedule_event_id")
    private List<ScheduleEventEntity> scheduleEventEntities;

}
