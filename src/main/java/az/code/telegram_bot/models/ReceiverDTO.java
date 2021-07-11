package az.code.telegram_bot.models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiverDTO {
    String UUID;
    byte[] file;
}
