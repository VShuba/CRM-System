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
public class UserOrganizationId implements Serializable { // це складовий ключ
    private Long userId;
    private Long organizationId;
}
