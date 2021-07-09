package az.code.telegram_bot.services;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.models.TourRequest;
import az.code.telegram_bot.services.Interfaces.FileService;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.TourRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Optional;

@Component
public class FileServiceImpl implements FileService {
    final
    MessageService messageService;

    final
    TourRequestService tourService;

    public FileServiceImpl(MessageService messageService, TourRequestService tourService) {
        this.messageService = messageService;
        this.tourService = tourService;
    }

    @Override
    public HttpStatus sendPhoto(MultipartFile file, String UUID, TelegramWebHook bot) throws IOException, TelegramApiException {
        Optional<TourRequest> tourRequest = tourService.getByUUID(UUID);
        if (tourRequest.isPresent()) {
            InputFile inputFile = new InputFile();
            inputFile.setMedia(file.getInputStream(), file.getName());
            bot.execute(SendPhoto.builder()
                    .chatId(tourRequest.get().getChatId())
                    .photo(inputFile).build());
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_ACCEPTABLE;
    }
}
