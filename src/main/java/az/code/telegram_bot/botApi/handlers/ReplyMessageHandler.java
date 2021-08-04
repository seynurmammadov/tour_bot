package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.exceptions.OfferShouldBeRepliedException;
import az.code.telegram_bot.models.AcceptedOffer;
import az.code.telegram_bot.models.AgencyOffer;
import az.code.telegram_bot.models.BotSession;
import az.code.telegram_bot.models.enums.StaticStates;
import az.code.telegram_bot.repositories.RedisRepository;
import az.code.telegram_bot.services.Interfaces.AgencyOfferService;
import az.code.telegram_bot.services.Interfaces.BotSessionService;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Optional;

@Component
public class ReplyMessageHandler implements MessageHandler {
    private Long userId;
    private Integer replyId;
    private String chatId;
    private TelegramWebHook bot;
    final
    MessageService messageService;
    final
    DataCache dataCache;
    final
    AgencyOfferService offerService;
    final
    QuestionService questionService;
    final
    MessageHandler inputMessageHandler;
    final
    RedisRepository<AcceptedOffer> acceptedOfferRepository;
    final
    BotSessionService sessionService;

    public ReplyMessageHandler(AgencyOfferService offerService, DataCache dataCache,
                               MessageService messageService, QuestionService questionService,
                               @Qualifier("inputMessageHandler") MessageHandler inputMessageHandler
            , RedisRepository<AcceptedOffer> acceptedOfferRepository, BotSessionService sessionService) {
        this.offerService = offerService;
        this.dataCache = dataCache;
        this.messageService = messageService;
        this.questionService = questionService;
        this.inputMessageHandler = inputMessageHandler;
        this.acceptedOfferRepository = acceptedOfferRepository;
        this.sessionService = sessionService;
    }


    @Override
    public SendMessage handle(Message message, TelegramWebHook bot, boolean isCommand) throws TelegramApiException, IOException {
        setData(message, bot);
        String UUID = dataCache.getUserData(userId).getUUID();
        Optional<AgencyOffer> offer = offerService.getByMessageIdAndUUID(this.replyId, UUID);
        if (offer.isPresent()) {
            return waitingStatusNQuestion(message, UUID, offer.get());
        } else {
            return messageService.createError(chatId,
                    new OfferShouldBeRepliedException(),
                    dataCache.getUserData(userId).getLangId());
        }
    }

    private SendMessage waitingStatusNQuestion(Message message, String UUID, AgencyOffer offer) throws TelegramApiException, IOException {
        Optional<BotSession> botSession = sessionService.getByUUID(UUID);
        if (botSession.isPresent()) {
            return checkContactInfo(message, offer);
        }
        return null;
    }

    private SendMessage checkContactInfo(Message message, AgencyOffer offer) throws TelegramApiException, IOException {
        AcceptedOffer acceptedOffer = acceptedOfferRepository.findById(userId);
        if (acceptedOffer == null) {
            return setContactQuestion(message, offer);
        } else {
            dataCache.setQuestion(userId,
                    questionService.getByKeyword(StaticStates.REPLY_END.toString()));
            message.setText(acceptedOffer.getPhoneNumber());
            acceptedOffer.setAgentUsername(offer.getUsername());
            acceptedOfferRepository.save(userId, acceptedOffer);
            return inputMessageHandler.handle(message, bot, true);
        }
    }

    private SendMessage setContactQuestion(Message message, AgencyOffer offer) throws TelegramApiException, IOException {
        dataCache.setQuestion(userId,
                questionService.getByKeyword(StaticStates.REPLY_START.toString()));
        acceptedOfferRepository.save(userId,
                AcceptedOffer.builder()
                        .agentUsername(offer.getUsername())
                        .firstName(message.getFrom().getFirstName())
                        .lastName(message.getFrom().getLastName())
                        .username(message.getFrom().getUserName())
                        .UUID(offer.getUUID())
                        .userId(this.userId)
                        .build());
        return inputMessageHandler.handle(message, bot, true);
    }

    private void setData(Message message, TelegramWebHook bot) {
        this.userId = message.getFrom().getId();
        this.chatId = message.getChatId().toString();
        this.replyId = message.getReplyToMessage().getMessageId();
        this.bot = bot;
    }
}
