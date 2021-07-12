package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.models.AgentOffer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

public interface ListenerService {
    void sendPhoto(AgentOffer agentOffer, TelegramWebHook bot) throws IOException, TelegramApiException;
    void sendNextPhotos(Long userId, TelegramWebHook bot) throws TelegramApiException, IOException;
}
