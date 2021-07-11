package az.code.telegram_bot.services;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.AgentOffer;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.TourRequest;
import az.code.telegram_bot.services.Interfaces.ListenerService;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import az.code.telegram_bot.services.Interfaces.TourRequestService;
import az.code.telegram_bot.utils.ButtonsUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Component
public class ListenerServiceImpl implements ListenerService {
    final
    MessageService messageService;

    final
    TourRequestService tourService;

    final
    AgentOfferImpl agentReceiver;

    final
    ButtonsUtil buttonsUtil;

    final
    QuestionService questionService;

    final
    DataCache dataCache;


    public ListenerServiceImpl(MessageService messageService, TourRequestService tourService,
                               AgentOfferImpl agentReceiver, ButtonsUtil buttonsUtil,
                               QuestionService questionService, DataCache dataCache) {
        this.messageService = messageService;
        this.tourService = tourService;
        this.agentReceiver = agentReceiver;
        this.buttonsUtil = buttonsUtil;
        this.questionService = questionService;
        this.dataCache = dataCache;
    }

    @Override
    public void sendPhoto(AgentOffer offer, TelegramWebHook bot) throws IOException, TelegramApiException {
        Optional<TourRequest> tourRequest = tourService.getByUUID(offer.getUUID());
        if (tourRequest.isPresent()) {
            InputStream inputStream = new ByteArrayInputStream(offer.getFile());
            InputFile inputFile = new InputFile();
            inputFile.setMedia(inputStream, "image");
            sendActions(offer, bot, tourRequest.get(), inputFile);
        }
    }

    private void sendActions(AgentOffer offer, TelegramWebHook bot,
                             TourRequest tourRequest, InputFile inputFile) throws TelegramApiException,
            IOException {
        int offersCount = tourRequest.getCountOffers();
        //TODO remove hard code
        if (offersCount <= 5 ) {
            bot.execute(SendPhoto.builder()
                    .chatId(tourRequest.getChatId())
                    .photo(inputFile).build());
        } else {
            sendNextButton(bot, tourRequest);
            savePhoto(offer);
        }
        tourRequest.setCountOffers(++offersCount);
        tourService.saveSeance(tourRequest);
    }

    //TODO remove hard code
    private void sendNextButton(TelegramWebHook bot, TourRequest tourRequest) throws TelegramApiException {
        Question nextQuestion = questionService.getQuestionByKeyword("next");
        long langId = dataCache.getUserProfileData(tourRequest.getClient_id()).getLangId();
        if (tourRequest.getNextMessageId() == null) {
            Message message = messageService.sendNextButton(bot, tourRequest, nextQuestion, langId);
            tourRequest.setNextMessageId(message.getMessageId().toString());
            tourService.saveSeance(tourRequest);
        } else {
            messageService.updateNextButton(bot, tourRequest, nextQuestion, langId);
        }
    }


    private void savePhoto(AgentOffer offer) throws IOException {
        //TODO remove hard code
        String path = "images/" + UUID.randomUUID() + ".jpg";
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(offer.getFile());
        }
        offer.setFilePath(path);
        agentReceiver.create(offer);
    }
}
