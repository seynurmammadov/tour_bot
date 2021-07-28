package az.code.telegram_bot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtil {
    @Value("${offer.responseLimit}")
    String responseLimit;

    public LocalDateTime addLimit(LocalDateTime time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd:HH:mm");
        LocalDateTime limit = LocalDateTime.parse(responseLimit, dtf);
        return time.plusDays(limit.getDayOfMonth()).plusHours(limit.getHour()).plusMinutes(limit.getMinute());
    }
}
