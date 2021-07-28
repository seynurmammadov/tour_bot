package az.code.telegram_bot.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bot_session")
@Builder
@ToString
public class BotSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String UUID;
    String chatId;
    Long client_id;
    int countOfOffers;
    int countOfSent;
    boolean lock;
    Integer nextMessageId;
    boolean status;
    LocalDateTime createdAt;
    boolean waitingAnswer;
    LocalDateTime expiredAt;
}
