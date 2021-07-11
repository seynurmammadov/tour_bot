package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.models.AgentOffer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface ListenerService {
    void sendPhoto(AgentOffer agentOffer, TelegramWebHook bot) throws IOException, TelegramApiException;
}
