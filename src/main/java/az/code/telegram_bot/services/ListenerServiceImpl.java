package az.code.telegram_bot.services;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.exceptions.NoOffersElseException;
import az.code.telegram_bot.exceptions.RequestWasStoppedException;
import az.code.telegram_bot.models.AgencyOffer;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.BotSession;
import az.code.telegram_bot.models.enums.QueryType;
import az.code.telegram_bot.services.Interfaces.*;
import az.code.telegram_bot.utils.FileUtil;
import az.code.telegram_bot.utils.MessageFakerUtil;
import az.code.telegram_bot.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ListenerServiceImpl implements ListenerService {
    final
    MessageService messageService;
    final
    BotSessionService sessionService;
    final
    AgencyOfferService offerService;
    final
    QuestionService questionService;
    final
    FileUtil fileUtil;
    final
    DataCache dataCache;
    @Value("${offer.sentCount}")
    String maxSentOfferCount;
    final
    TimeUtil timeUtil;
    final
    MessageFakerUtil fakerUtil;
    final
    MessageHandler commandHandler;

    private TelegramWebHook bot;


    public ListenerServiceImpl(MessageService messageService, BotSessionService sessionService,
                               AgencyOfferImpl offerService, QuestionService questionService,
                               DataCache dataCache, FileUtil fileUtil,
                               @Qualifier("commandHandler") MessageHandler commandHandler, TimeUtil timeUtil, MessageFakerUtil fakerUtil) {
        this.messageService = messageService;
        this.sessionService = sessionService;
        this.offerService = offerService;
        this.questionService = questionService;
        this.dataCache = dataCache;
        this.fileUtil = fileUtil;
        this.commandHandler = commandHandler;
        this.timeUtil = timeUtil;
        this.fakerUtil = fakerUtil;
    }

    @Override
    public void sendPhoto(AgencyOffer offer, TelegramWebHook bot) throws IOException, TelegramApiException {
        Optional<BotSession> botSession = sessionService.getByUUID(offer.getUUID());
        if (botSession.isPresent()) {
            setData(bot);
            botSession.get().setCountOfOffers(botSession.get().getCountOfOffers() + 1);
            sendActions(offer, botSession.get(), fileUtil.byteArrToInputFile(offer.getFile()));
        }
    }

    @Override
    public void sendExpiredNotification(String UUID, TelegramWebHook bot) throws TelegramApiException, IOException {
        Optional<BotSession> botSession = sessionService.getByUUID(UUID);
        if (botSession.isPresent()) {
            setData(bot);
            long landId = dataCache.getUserData(botSession.get().getClient_id()).getLangId();
            String chatId = botSession.get().getChatId();
            if (botSession.get().getCountOfOffers() > 0) {
                changeSessionStatus(bot, botSession.get(), landId, chatId);
            } else {
                stopRequest(botSession.get(), landId, chatId);
            }
        }
    }

    private void stopRequest(BotSession botSession, long landId, String chatId) throws TelegramApiException, IOException {
        bot.execute(
                messageService.createNotify(
                        chatId,
                        new RequestWasStoppedException(),
                        landId)
        );
        commandHandler.handle(fakerUtil.fakeStop(botSession, chatId), bot, true);
    }

    private void changeSessionStatus(TelegramWebHook bot, BotSession botSession, long landId, String chatId) throws TelegramApiException {
        bot.execute(
                messageService.createNotify(
                        chatId,
                        new NoOffersElseException(),
                        landId)
        );
        botSession.setWaitingAnswer(true);
        botSession.setExpiredAt(timeUtil.addLimit(LocalDateTime.now()));
        sessionService.save(botSession);
    }

    private void sendActions(AgencyOffer offer, BotSession botSession, InputFile inputFile) throws TelegramApiException, IOException {
        int offersCount = botSession.getCountOfOffers();
        if (!botSession.isLock()) {
            sendMessageNSaveOffer(offer, botSession.getChatId(), inputFile);
            if (offersCount % Integer.parseInt(maxSentOfferCount) == 0) {
                botSession.setLock(true);
            }
            botSession.setCountOfSent(botSession.getCountOfSent() + 1);
        } else {
            sendNextButton(botSession);
            savePhoto(offer);
        }
        sessionService.save(botSession);
    }

    @Override
    public void sendNextPhotos(Long userId, TelegramWebHook bot) throws TelegramApiException, IOException {
        Optional<BotSession> botSession = sessionService.getByUserId(userId);
        if (botSession.isPresent()) {
            setData(bot);
            botSession.get().setLock(false);
            sendPhotoFromDb(botSession.get());
        }
    }

    private void sendPhotoFromDb(BotSession botSession) throws IOException, TelegramApiException {
        List<AgencyOffer> agencyOffers = offerService.getAllByUUID(botSession.getUUID());
        for (int i = 0; i < agencyOffers.size(); i++) {
            if (i >= Integer.parseInt(maxSentOfferCount)) {
                botSession.setNextMessageId(null);
                sendNextButton(botSession);
                return;
            }
            sendNextPhoto(botSession, agencyOffers.get(i));
        }
        botSession.setNextMessageId(null);
        sessionService.save(botSession);
    }

    private void sendNextPhoto(BotSession botSession, AgencyOffer offer)
            throws IOException, TelegramApiException {
        InputFile inputFile = fileUtil.pathToInputFile(offer.getFilePath());
        sendActions(offer, botSession, inputFile);
        fileUtil.deleteWithPath(offer.getFilePath());
        offer.setFilePath(null);
        offerService.save(offer);
    }


    private void sendMessageNSaveOffer(AgencyOffer offer, String chatId, InputFile inputFile) throws TelegramApiException {
        Message message = this.bot.execute(messageService.createPhoto(chatId, inputFile));
        offer.setMessageId(message.getMessageId());
        offerService.save(offer);
    }

    private void sendNextButton(BotSession botSession) throws TelegramApiException {
        Question nextQuestion = questionService.getByKeyword(QueryType.NEXT.toString());
        long langId = dataCache.getUserData(botSession.getClient_id()).getLangId();
        if (botSession.getNextMessageId() == null) {
            Message message = bot.execute(messageService.createNextBtn(botSession, nextQuestion, langId));
            botSession.setNextMessageId(message.getMessageId());
            sessionService.save(botSession);
        } else {
            bot.execute(messageService.updateNextBtn(botSession, nextQuestion, langId));
        }
    }


    private void savePhoto(AgencyOffer offer) throws IOException {
        offer.setFilePath(fileUtil.savePhoto(offer.getFile()));
        offerService.save(offer);
    }

    private void setData(TelegramWebHook bot) {
        this.bot = bot;
    }


}
