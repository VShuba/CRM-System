package ua.shpp.entity;


import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventClientId {
    private Long clientId;
    private Long eventId;

}
