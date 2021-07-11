package az.code.telegram_bot.services;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.models.ReceiverDTO;
import az.code.telegram_bot.models.TourRequest;
import az.code.telegram_bot.services.Interfaces.ListeningService;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.TourRequestService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Component
public class ListenerServiceImpl implements ListeningService {
    final
    MessageService messageService;

    final
    TourRequestService tourService;

    private final RabbitTemplate template;

    public ListenerServiceImpl(MessageService messageService, TourRequestService tourService, RabbitTemplate template) {
        this.messageService = messageService;
        this.tourService = tourService;
        this.template = template;
    }

    @Override
    public void sendPhoto(ReceiverDTO receiverDTO, TelegramWebHook bot) throws IOException, TelegramApiException {
        Optional<TourRequest> tourRequest = tourService.getByUUID(receiverDTO.getUUID());
        if (tourRequest.isPresent()) {
            InputStream inputStream = new ByteArrayInputStream(receiverDTO.getFile());
            InputFile inputFile = new InputFile();
            inputFile.setMedia(inputStream,"image");
            bot.execute(SendPhoto.builder()
                    .chatId(tourRequest.get().getChatId())
                    .photo(inputFile).build());
        }
    }
}
