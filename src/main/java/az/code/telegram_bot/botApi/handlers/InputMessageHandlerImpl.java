package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.exceptions.IncorrectAnswerException;
import az.code.telegram_bot.exceptions.MaxStringSizeException;
import az.code.telegram_bot.models.Action;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.enums.ActionType;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;


@Component
public class InputMessageHandlerImpl implements MessageHandler {
    final
    MessageService messageService;
    final
    QuestionService questionService;
    final
    DataCache dataCache;

    private Long userId;
    private String chatId;
    private TelegramWebHook bot;

    public InputMessageHandlerImpl(MessageService messageService, QuestionService questionService,
                                   DataCache dataCache) {
        this.messageService = messageService;
        this.questionService = questionService;
        this.dataCache = dataCache;
    }

    /**
     * @param message
     * @param bot
     * @return question with actionType, if was last question or if user answer is incorrect {@code null}
     * @throws TelegramApiException
     */
    @Override
    public SendMessage handle(Message message, TelegramWebHook bot) throws TelegramApiException {
        setData(message, bot);
        //TODO set length of text to property
        if (message.getText().length() > 50) {
            return messageService.createError(chatId,
                    new MaxStringSizeException(50),
                    dataCache.getUserProfileData(userId).getLangId());
        }
        Question currentQuestion = dataCache.getCurrentQuestion(userId);
        if (!checkAnswerNCaching(currentQuestion, message))
            return null;
        else
            currentQuestion = dataCache.getCurrentQuestion(userId);
        long currentLanguage =dataCache.getUserProfileData(userId).getLangId();
        return getMessage(currentQuestion, currentLanguage);
    }

    /**
     * <p>This method checks if the user's answer is correct and caches the answer if the answer is.</p>
     * <p>If the answer is not correct, an appropriate message is sent.</p>
     *
     * @param state
     * @param message
     * @return {@code true}  If answer is present {@code false} if answer is present and this question
     * was last or answer is not present
     * @throws TelegramApiException
     */
    private boolean checkAnswerNCaching(Question state, Message message) throws TelegramApiException {

        Optional<Action> filteredAnswer = state.getActions().stream()
                .filter(a -> a.getActionTranslates().stream()
                        .anyMatch(t -> t.getContext().equals(message.getText())))
                .findFirst();
        Action actionFreeText = state.getActions().iterator().next();
        if (filteredAnswer.isPresent()) {
            return !cachingData(state, message, filteredAnswer.get());
        } else if (actionFreeText.getType() == ActionType.FREETEXT) {
            return !cachingData(state, message, actionFreeText);
        } else if (state.getState() != null) {
            bot.execute(messageService.createError(chatId,
                    new IncorrectAnswerException(),
                    dataCache.getUserProfileData(userId).getLangId()));
            return false;
        }
        return true;
    }

    /**
     * This method caches data and if question was last then send collected data to user
     *
     * @param state
     * @param message
     * @param filteredAnswer
     * @return {@code true}  if question was last
     * @throws TelegramApiException
     */
    private boolean cachingData(Question state, Message message, Action filteredAnswer) throws TelegramApiException {
        if (state.getState() == null) {
            dataCache.setLanguage(userId, message.getText());
        } else {
            dataCache.addAnswer(userId, message.getText());
        }
        dataCache.setQuestion(userId, filteredAnswer.getNextQuestion());
        if (sendCollectedData(filteredAnswer)) return true;
        return false;
    }

    /**
     * This method send collected user data
     *
     * @param filteredAnswer
     * @return {@code true}  if the data sent {@code false} if data not sent
     * @throws TelegramApiException
     */
    private boolean sendCollectedData(Action filteredAnswer) throws TelegramApiException {
        if (filteredAnswer.getNextQuestion() == null) {
            messageService.sendData(chatId, userId, dataCache.getUserProfileData(userId).toString(), bot);
            return true;
        }
        return false;
    }

    /**
     * This method creates a message dynamically using data from the database.
     *
     * @param question
     * @param langId
     * @return Message which created by actionType
     */
    private SendMessage getMessage(Question question, Long langId) {
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

    /**
     * This method sets data that is used in other methods.
     *
     * @param message
     * @param bot
     */
    private void setData(Message message, TelegramWebHook bot) {
        this.userId = message.getFrom().getId();
        this.chatId = message.getChatId().toString();
        this.bot = bot;
    }

}
