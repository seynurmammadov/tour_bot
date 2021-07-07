package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.enums.ActionType;
import az.code.telegram_bot.models.enums.BotState;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import az.code.telegram_bot.utils.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class InputMessageHandlerImpl implements MessageHandler {
    final
    MessageService messageService;
    final
    QuestionService questionService;
    final
    DataCache dataCache;

    public InputMessageHandlerImpl(MessageService messageService, QuestionService questionService, DataCache dataCache) {
        this.messageService = messageService;
        this.questionService = questionService;
        this.dataCache = dataCache;
    }

    @Override
    public SendMessage handle(Message message, BotState botState, Long userId) {
        SendMessage sendMessage;
        Long langId = dataCache.getUserProfileData(userId).getLangId();

        Question question = questionService.getQuestionByKeyword(botState);
        String chatId = message.getChatId().toString();
        ActionType actionType = question.getActions().stream().findFirst().get().getType();

        switch (actionType) {
            case FREETEXT:
                sendMessage = messageService.simpleMessage(chatId, question, langId);
                break;
            case BUTTON:
                sendMessage = messageService.msgWithRepKeyboard(chatId,question,langId);
                break;
            case INLINE_BUTTON:
                sendMessage = messageService.msgWithInlKeyboard(chatId, question, langId);
                break;
            case CALENDAR:
                sendMessage = messageService.createCalendar(chatId, question, langId);
                break;
            default:
                sendMessage = null;
        }

        return sendMessage;
    }
}
