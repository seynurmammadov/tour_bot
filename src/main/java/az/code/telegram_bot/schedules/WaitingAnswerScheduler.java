package az.code.telegram_bot.schedules;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.models.BotSession;
import az.code.telegram_bot.repositories.BotSessionRepository;
import az.code.telegram_bot.utils.MessageFakerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;


@Component
@Slf4j
public class WaitingAnswerScheduler {
    final
    BotSessionRepository sessionRepository;
    final
    MessageHandler commandHandler;
    final
    MessageFakerUtil fakerUtil;
    final
    TelegramWebHook telegramWebHook;

    public WaitingAnswerScheduler(BotSessionRepository sessionRepository,
                                  @Qualifier("commandHandler") MessageHandler commandHandler, MessageFakerUtil fakerUtil, TelegramWebHook telegramWebHook) {
        this.sessionRepository = sessionRepository;
        this.commandHandler = commandHandler;
        this.fakerUtil = fakerUtil;
        this.telegramWebHook = telegramWebHook;
    }

    @Scheduled(cron = "${cron.waitingAnswerScheduler}", zone = "Asia/Baku")
    public void waitingAnswerScheduler() throws TelegramApiException, IOException {
        for (BotSession session:sessionRepository.getNotAnsweredSessions()) {
            commandHandler.handle(fakerUtil.fakeStop(session, session.getChatId()), telegramWebHook, true);
        }
    }

}
