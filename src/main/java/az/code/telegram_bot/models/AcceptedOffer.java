package az.code.telegram_bot.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcceptedOffer {
    String UUID;
    String agencyName;
    String phoneNumber;
    String userName;
    String firstName;
    String lastName;
    Long userId;
}
