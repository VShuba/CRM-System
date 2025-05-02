package ua.shpp.entity;


import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventClientId implements Serializable {
    private Long clientId;
    private Long eventId;
}
