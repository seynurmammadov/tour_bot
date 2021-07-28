package az.code.telegram_bot.schedules;

import az.code.telegram_bot.repositories.BotSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class WaitingAnswerScheduler {
    final
    BotSessionRepository sessionRepository;

    public WaitingAnswerScheduler(BotSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Scheduled(cron = "${cron.waitingAnswerScheduler}", zone = "Asia/Baku")
    public void waitingAnswerScheduler() {
        sessionRepository.getNotAnsweredSessions().forEach(System.out::println);
    }
}
