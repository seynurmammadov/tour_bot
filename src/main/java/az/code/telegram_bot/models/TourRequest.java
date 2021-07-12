package az.code.telegram_bot.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tour_request")
@Builder
public class TourRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String UUID;
    String chatId;
    Long client_id;
    int countOfOffers;
    int countOfSent;
    boolean lock;
    String nextMessageId;
    boolean status;
    LocalDateTime createdAt;
}
