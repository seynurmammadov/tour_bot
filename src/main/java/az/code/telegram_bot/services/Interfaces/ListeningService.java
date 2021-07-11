package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.models.ReceiverDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface ListeningService {
   void sendPhoto(ReceiverDTO receiverDTO, TelegramWebHook bot) throws IOException, TelegramApiException;
}
