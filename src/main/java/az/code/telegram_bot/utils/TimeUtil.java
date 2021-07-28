package az.code.telegram_bot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeUtil {
    @Value("${offer.responseLimit.hours}")
    String hours;
    @Value("${offer.responseLimit.minutes}")
    String minutes;
    @Value("${offer.responseLimit.days}")
    String days;
    public LocalDateTime addLimit(LocalDateTime time) {
        return time
                .plusDays(Long.parseLong(days))
                .plusHours(Long.parseLong(hours))
                .plusMinutes(Long.parseLong(minutes));
    }
}
