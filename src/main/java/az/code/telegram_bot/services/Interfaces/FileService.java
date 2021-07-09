package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.TelegramWebHook;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface FileService {
   HttpStatus sendPhoto(MultipartFile file, String UUID, TelegramWebHook bot) throws IOException, TelegramApiException;
}
