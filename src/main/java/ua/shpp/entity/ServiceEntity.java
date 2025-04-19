package ua.shpp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "services")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color; // HEX-code - like #FF0000

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private BranchEntity branch;

    @ManyToMany
    @JoinTable(
            name = "service_room",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Set<RoomEntity> rooms = new HashSet<>();

    @ManyToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SubscriptionServiceEntity> subscriptions = new ArrayList<>();

    @OneToMany(
            mappedBy = "activity",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<OneTimeServiceEntity> oneTimeServices = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        for (SubscriptionServiceEntity sub : new ArrayList<>(subscriptions)) {
            sub.getActivity().remove(this);
        }
    }

    @ManyToMany(mappedBy = "services")
    Set<EmployeeEntity> employees; // сделать тоже самое что и с комнатами
}