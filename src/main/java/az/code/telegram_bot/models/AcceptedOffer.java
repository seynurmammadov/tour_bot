package az.code.telegram_bot.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcceptedOffer implements Serializable {
    String UUID;
    String username;
    String phoneNumber;
    String userName;
    String firstName;
    String lastName;
    Long userId;
}
