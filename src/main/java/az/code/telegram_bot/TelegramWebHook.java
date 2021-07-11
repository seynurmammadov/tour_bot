package az.code.telegram_bot;

import az.code.telegram_bot.botApi.TelegramFacade;
import az.code.telegram_bot.configs.RabbitMQConfig;
import az.code.telegram_bot.models.ReceiverDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

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


    @RabbitListener(queues = RabbitMQConfig.receiver)
    public void sendPhoto(ReceiverDTO receiverDTO) throws IOException, TelegramApiException {
         telegramFacade.sendPhoto(receiverDTO,this);
    }
}
