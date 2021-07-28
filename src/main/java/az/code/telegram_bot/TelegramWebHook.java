package az.code.telegram_bot;

import az.code.telegram_bot.botApi.TelegramFacade;
import az.code.telegram_bot.configs.RabbitMQConfig;
import az.code.telegram_bot.models.AgencyOffer;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Slf4j
@Builder
public class TelegramWebHook extends TelegramWebhookBot {
    private final String webHookPath;
    private final String botUsername;
    private final String botToken;
    private final String botPath;
    private final TelegramFacade telegramFacade;

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update, this);
    }

    @RabbitListener(queues = RabbitMQConfig.offered)
    public void sendPhoto(AgencyOffer agencyOffer) throws IOException, TelegramApiException {
        telegramFacade.sendPhoto(agencyOffer, this);
    }
//    @RabbitListener(queues = RabbitMQConfig.expired)
    public void sendExpiredNotification(String UUID) throws TelegramApiException, IOException {
        telegramFacade.sendExpiredNotification(UUID, this);
    }
}
