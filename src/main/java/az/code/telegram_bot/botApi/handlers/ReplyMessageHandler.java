package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.exceptions.OfferShouldBeRepliedException;
import az.code.telegram_bot.models.AgencyOffer;
import az.code.telegram_bot.models.enums.StaticStates;
import az.code.telegram_bot.services.Interfaces.AgencyOfferService;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ReplyMessageHandler(AgencyOfferService offerService, DataCache dataCache,
                               MessageService messageService, QuestionService questionService,
                               @Qualifier("inputMessageHandler") MessageHandler inputMessageHandler) {
        this.offerService = offerService;
        this.dataCache = dataCache;
        this.messageService = messageService;
        this.questionService = questionService;
        this.inputMessageHandler = inputMessageHandler;
    }


    @Override
    public SendMessage handle(Message message, TelegramWebHook bot, boolean isCommand) throws TelegramApiException {
        setData(message, bot);
        String UUID = dataCache.getUserProfileData(userId).getUUID();
        Optional<AgencyOffer> offer = offerService.getByMessageIdAndUUID(this.replyId, UUID);
        if (offer.isPresent()) {
            dataCache.setQuestion(userId, questionService.getQuestionByKeyword(StaticStates.REPLY_START.toString()));
            System.out.println(StaticStates.REPLY_START.toString());
            System.out.println( questionService.getQuestionByKeyword(StaticStates.REPLY_START.toString()).getState());
            return inputMessageHandler.handle(message, bot, true);
        } else {
            return messageService.createError(chatId, new OfferShouldBeRepliedException(), dataCache.getUserProfileData(userId).getLangId());
        }
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
