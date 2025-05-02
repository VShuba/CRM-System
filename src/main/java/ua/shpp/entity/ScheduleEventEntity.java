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

    // Додано, щоб прив’язати конкретну подію до співробітника, який її проводить
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    // Додано, щоб зв’язати конкретну подію з кімнатою, де вона відбувається
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_type_id")
    private EventTypeEntity eventType;

    //todo fix service what was added
}
