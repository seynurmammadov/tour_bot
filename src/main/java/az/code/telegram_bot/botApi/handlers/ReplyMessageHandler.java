package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.exceptions.OfferShouldBeRepliedException;
import az.code.telegram_bot.models.AcceptedOffer;
import az.code.telegram_bot.models.AgencyOffer;
import az.code.telegram_bot.models.enums.StaticStates;
import az.code.telegram_bot.repositories.RedisRepository;
import az.code.telegram_bot.services.Interfaces.AgencyOfferService;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

    public ReplyMessageHandler(AgencyOfferService offerService, DataCache dataCache,
                               MessageService messageService, QuestionService questionService,
                               @Qualifier("inputMessageHandler") MessageHandler inputMessageHandler
            , RedisRepository<AcceptedOffer> acceptedOfferRepository) {
        this.offerService = offerService;
        this.dataCache = dataCache;
        this.messageService = messageService;
        this.questionService = questionService;
        this.inputMessageHandler = inputMessageHandler;
        this.acceptedOfferRepository = acceptedOfferRepository;
    }


    @Override
    public SendMessage handle(Message message, TelegramWebHook bot, boolean isCommand)
            throws TelegramApiException {
        setData(message, bot);
        String UUID = dataCache.getUserProfileData(userId).getUUID();
        Optional<AgencyOffer> offer = offerService.getByMessageIdAndUUID(this.replyId, UUID);
        if (offer.isPresent()) {
            return setContactQuestion(message, bot, offer);
        } else {
            return messageService.createError(chatId,
                    new OfferShouldBeRepliedException(),
                    dataCache.getUserProfileData(userId).getLangId());
        }
    }

    private SendMessage setContactQuestion(Message message, TelegramWebHook bot, Optional<AgencyOffer> offer) throws TelegramApiException {
        dataCache.setQuestion(userId,
                questionService.getQuestionByKeyword(StaticStates.REPLY_START.toString()));
        acceptedOfferRepository.save(userId,
                AcceptedOffer.builder()
                        .agencyName(offer.get().getAgencyName())
                        .firstName(message.getFrom().getFirstName())
                        .lastName(message.getFrom().getLastName())
                        .userName(message.getFrom().getUserName())
                        .UUID(offer.get().getUUID())
                        .userId(this.userId)
                        .build());
        return inputMessageHandler.handle(message, bot, true);
    }

    /**
     * Method sets data that is used in other methods.
     *
     * @param message user response message
     * @param bot     TelegramWebHook for sending additional messages
     */
    private void setData(Message message, TelegramWebHook bot) {
        this.userId = message.getFrom().getId();
        this.chatId = message.getChatId().toString();
        this.replyId = message.getReplyToMessage().getMessageId();
        this.bot = bot;
    }
}
