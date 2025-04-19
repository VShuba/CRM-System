package ua.shpp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "event_types")
public class EventTypeEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "eventType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<OneTimeServiceEntity> oneTimeVisits;

    @OneToMany(mappedBy = "eventType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SubscriptionServiceEntity> subscriptions;

    //todo add BranchEntity
}
