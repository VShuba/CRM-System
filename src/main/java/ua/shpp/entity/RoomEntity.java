package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "rooms")
    private Set<ServiceEntity> services = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private BranchEntity branch;

    @OneToMany(mappedBy = "room")
    @Column(name = "schedule_event_id")
    private List<ScheduleEventEntity> scheduleEventEntities;

}
