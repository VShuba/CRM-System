package ua.shpp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "schedule_event")
public class ScheduleEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //  Обрати приміщення
    //  Обрати працівника
    //  Обрати тип заходу
    //  Обрати послугу
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity serviceEntity;
    //  Налаштувати час провещення івенту
    private LocalDate eventDate;
    //  Обрати часовий інтерва
    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;
    //  Обрати повторення
    @Column(name = "repeat_event")
    private Boolean repeatEvent;
    //   Налаштувати максимальну кількість людей для запису
    @Column(name = "number_of_people")
    private Byte numberOfPeople;

    @OneToMany(mappedBy = "scheduleEvent")
    @Column(name = "event_clients")
    private List<EventClientEntity> eventClients;
}
