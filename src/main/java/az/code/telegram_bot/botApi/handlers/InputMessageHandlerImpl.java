package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.Action;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.enums.ActionType;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import lombok.Builder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Builder
@Component
public class InputMessageHandlerImpl implements MessageHandler {
    final
    MessageService messageService;
    final
    QuestionService questionService;
    final
    DataCache dataCache;


    public InputMessageHandlerImpl(MessageService messageService, QuestionService questionService,
                                   DataCache dataCache) {
        this.messageService = messageService;
        this.questionService = questionService;
        this.dataCache = dataCache;
    }

    @Override
    public SendMessage handle(Message message, Question state,
                              String chatId, TelegramWebHook bot) throws TelegramApiException {
        long userId = message.getFrom().getId();
        if (!checkAnswer(state, message, chatId, bot))
            return null;
        else
            state = dataCache.getState(userId);
        return getMessage(state, chatId, dataCache.getUserProfileData(userId).getLangId());
    }

    private boolean checkAnswer(Question state, Message message,
                                String chatId, TelegramWebHook bot) throws TelegramApiException {

        long userId = message.getFrom().getId();
        Optional<Action> actionFiltered = state.getActions().stream()
                .filter(a -> a.getActionTranslates().stream()
                        .anyMatch(t -> t.getContext().equals(message.getText())))
                .findFirst();
        if (actionFiltered.isPresent()) {
            dataCache.setState(userId, actionFiltered.get().getNextQuestion());
            if (state.getState() == null) {
                dataCache.setLanguage(userId, message.getText());
                return true;
            }
            if (actionFiltered.get().getNextQuestion() == null) {
                bot.execute(new SendMessage(chatId, dataCache.getUserProfileData(userId).toString()));
                return false;
            }
        } else if (state.getState() != null) {
            bot.execute(new SendMessage(chatId, "Incorrect"));
            return false;
        }
        return true;
    }

    private SendMessage getMessage(Question question, String chatId, Long langId) {
        ActionType actionType = question.getActions()
                .stream()
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getType();

        switch (actionType) {
            case FREETEXT:
                return messageService.simpleQuestionMessage(chatId, question, langId);
            case BUTTON:
                return messageService.msgWithRepKeyboard(chatId, question, langId);
            case INLINE_BUTTON:
                return messageService.msgWithInlKeyboard(chatId, question, langId);
            case CALENDAR:
                return messageService.createCalendar(chatId, question, langId);
            default:
                return null;
        }
    }

}
