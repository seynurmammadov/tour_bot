package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.models.AgencyOffer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface ListenerService {
    void sendPhoto(AgencyOffer agencyOffer, TelegramWebHook bot) throws IOException, TelegramApiException;
    void sendNextPhotos(Long userId, TelegramWebHook bot) throws TelegramApiException, IOException;
}
