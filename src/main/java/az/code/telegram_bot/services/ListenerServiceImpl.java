package az.code.telegram_bot.services;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.AgencyOffer;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.BotSession;
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
    BotSessionService sessionService;

    final
    AgencyOfferService offerService;

    final
    ButtonsUtil buttonsUtil;

    final
    QuestionService questionService;

    final
    DataCache dataCache;


    public ListenerServiceImpl(MessageService messageService, BotSessionService sessionService,
                               AgencyOfferImpl offerService, ButtonsUtil buttonsUtil,
                               QuestionService questionService, DataCache dataCache) {
        this.messageService = messageService;
        this.sessionService = sessionService;
        this.offerService = offerService;
        this.buttonsUtil = buttonsUtil;
        this.questionService = questionService;
        this.dataCache = dataCache;
    }

    @Override
    public void sendPhoto(AgencyOffer offer, TelegramWebHook bot) throws IOException, TelegramApiException {
        Optional<BotSession> tourRequest = sessionService.getByUUID(offer.getUUID());
        if (tourRequest.isPresent()) {
            InputFile inputFile = getInputFile(offer.getFile());
            tourRequest.get().setCountOfOffers(tourRequest.get().getCountOfOffers() + 1);
            sendActions(offer, bot, tourRequest.get(), inputFile);
        }
    }

    @Override
    public void sendNextPhotos(Long userId, TelegramWebHook bot) throws TelegramApiException, IOException {
        Optional<BotSession> tourRequest = sessionService.getByUserId(userId);
        if (tourRequest.isPresent()) {
            List<AgencyOffer> agencyOffers = offerService.getAllByUUID(tourRequest.get().getUUID());
            tourRequest.get().setLock(false);
            sendPhotoFromDb(bot, tourRequest.get(), agencyOffers);
        }
    }

    //TODO remove hard code
    private void sendPhotoFromDb(TelegramWebHook bot, BotSession botSession,
                                 List<AgencyOffer> agencyOffers) throws IOException, TelegramApiException {

        for (int i = 0; i < agencyOffers.size(); i++) {
            if (i >= 5) {
                botSession.setNextMessageId(null);
                sendNextButton(bot, botSession);
                return;
            }
            AgencyOffer offer = agencyOffers.get(i);
            InputFile inputFile = getInputFile(Files.readAllBytes(Paths.get(offer.getFilePath())));
            sendActions(offer, bot, botSession, inputFile);
            Files.deleteIfExists(Paths.get(offer.getFilePath()));
            offer.setFilePath(null);
            offerService.save(offer);
        }
        botSession.setNextMessageId(null);
        sessionService.saveSeance(botSession);
    }

    private void sendActions(AgencyOffer offer, TelegramWebHook bot,
                             BotSession botSession, InputFile inputFile) throws TelegramApiException, IOException {
        int offersCount = botSession.getCountOfOffers();
        //TODO remove hard code
        if (!botSession.isLock()) {
            Message message = bot.execute(SendPhoto.builder()
                    .chatId(botSession.getChatId())
                    .photo(inputFile).build());
            offer.setMessageId(message.getMessageId());
            offerService.save(offer);
            if (offersCount % 5 == 0) {
                botSession.setLock(true);
            }
            botSession.setCountOfSent(botSession.getCountOfSent() + 1);

        } else {
            sendNextButton(bot, botSession);
            savePhoto(offer);
        }
        sessionService.saveSeance(botSession);
    }

    //TODO remove hard code
    private void sendNextButton(TelegramWebHook bot, BotSession botSession) throws TelegramApiException {
        Question nextQuestion = questionService.getQuestionByKeyword("next");
        long langId = dataCache.getUserProfileData(botSession.getClient_id()).getLangId();
        if (botSession.getNextMessageId() == null) {
            Message message = messageService.sendNextButton(bot, botSession, nextQuestion, langId);
            botSession.setNextMessageId(message.getMessageId());
            sessionService.saveSeance(botSession);
        } else {
            messageService.updateNextButton(bot, botSession, nextQuestion, langId);
        }
    }


    private void savePhoto(AgencyOffer offer) throws IOException {
        //TODO remove hard code
        String path = "images/" + UUID.randomUUID() + ".jpg";
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(offer.getFile());
        }
        offer.setFilePath(path);
        offerService.save(offer);
    }

    private InputFile getInputFile(byte[] array) {
        InputStream inputStream = new ByteArrayInputStream(array);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(inputStream, "image");
        return inputFile;
    }

}
