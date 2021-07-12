package az.code.telegram_bot.services;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.AgentOffer;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.TourRequest;
import az.code.telegram_bot.services.Interfaces.*;
import az.code.telegram_bot.utils.ButtonsUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ListenerServiceImpl implements ListenerService {
    final
    MessageService messageService;

    final
    TourRequestService tourService;

    final
    AgentOfferService offerService;

    final
    ButtonsUtil buttonsUtil;

    final
    QuestionService questionService;

    final
    DataCache dataCache;


    public ListenerServiceImpl(MessageService messageService, TourRequestService tourService,
                               AgentOfferImpl offerService, ButtonsUtil buttonsUtil,
                               QuestionService questionService, DataCache dataCache) {
        this.messageService = messageService;
        this.tourService = tourService;
        this.offerService = offerService;
        this.buttonsUtil = buttonsUtil;
        this.questionService = questionService;
        this.dataCache = dataCache;
    }

    @Override
    public void sendPhoto(AgentOffer offer, TelegramWebHook bot) throws IOException, TelegramApiException {
        Optional<TourRequest> tourRequest = tourService.getByUUID(offer.getUUID());
        if (tourRequest.isPresent()) {
            InputFile inputFile = getInputFile(offer.getFile());
            tourRequest.get().setCountOfOffers(tourRequest.get().getCountOfOffers() + 1);
            sendActions(offer, bot, tourRequest.get(), inputFile);
        }
    }

    @Override
    public void sendNextPhotos(Long userId, TelegramWebHook bot) throws TelegramApiException, IOException {
        Optional<TourRequest> tourRequest = tourService.getByUserId(userId);
        if (tourRequest.isPresent()) {
            List<AgentOffer> agentOffers = offerService.getAllByUUID(tourRequest.get().getUUID());
            tourRequest.get().setLock(false);
            sendPhotoFromDb(bot, tourRequest.get(), agentOffers);
        }
    }

    //TODO remove hard code
    private void sendPhotoFromDb(TelegramWebHook bot, TourRequest tourRequest,
                                 List<AgentOffer> agentOffers) throws IOException, TelegramApiException {

        for (int i = 0; i < agentOffers.size(); i++) {
            if (i >= 5) {
                tourRequest.setNextMessageId(null);
                sendNextButton(bot,tourRequest);
                return;
            }
            AgentOffer offer = agentOffers.get(i);
            InputFile inputFile = getInputFile(Files.readAllBytes(Paths.get(offer.getFilePath())));
            sendActions(offer, bot, tourRequest, inputFile);
            Files.deleteIfExists(Paths.get(offer.getFilePath()));
            offerService.deleteOffer(offer);
        }
        tourRequest.setNextMessageId(null);
        tourService.saveSeance(tourRequest);
    }

    private void sendActions(AgentOffer offer, TelegramWebHook bot,
                             TourRequest tourRequest, InputFile inputFile) throws TelegramApiException, IOException {
        int offersCount = tourRequest.getCountOfOffers();
        //TODO remove hard code
        if (!tourRequest.isLock()) {
            bot.execute(SendPhoto.builder()
                    .chatId(tourRequest.getChatId())
                    .photo(inputFile).build());
            if (offersCount % 5 == 0) {
                tourRequest.setLock(true);
            }
            tourRequest.setCountOfSended(tourRequest.getCountOfSended() + 1);
        } else {
            sendNextButton(bot, tourRequest);
            savePhoto(offer);
        }
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
        offerService.create(offer);
    }

    private InputFile getInputFile(byte[] array) {
        InputStream inputStream = new ByteArrayInputStream(array);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(inputStream, "image");
        return inputFile;
    }

}
