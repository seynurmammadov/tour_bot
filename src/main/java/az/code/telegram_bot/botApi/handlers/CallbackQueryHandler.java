package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.botApi.handlers.interfaces.QueryHandler;
import az.code.telegram_bot.cache.DataCacheImpl;
import az.code.telegram_bot.exceptions.BeforeCurrentDateException;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.enums.ActionType;
import az.code.telegram_bot.models.enums.QueryType;
import az.code.telegram_bot.services.Interfaces.ListenerService;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.utils.CalendarUtil;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class CallbackQueryHandler implements QueryHandler {
    final
    DataCacheImpl dataCache;
    final
    MessageService messageService;
    final
    MessageHandler inputMessageHandler;

    private Long userId;
    private Long langId;
    private String chatId;
    private TelegramWebHook bot;


    final
    ListenerService listenerService;

    public CallbackQueryHandler(DataCacheImpl dataCache, ListenerService listenerService,
                                MessageService messageService,
                                @Qualifier("inputMessageHandler") MessageHandler inputMessageHandler) {
        this.dataCache = dataCache;
        this.listenerService = listenerService;
        this.messageService = messageService;
        this.inputMessageHandler = inputMessageHandler;
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery buttonQuery, TelegramWebHook bot) throws TelegramApiException, IOException {
        setData(buttonQuery, bot);
        BotApiMethod<?> message = regexInlineButton(buttonQuery);
        if (message != null) {
            return regexInlineButton(buttonQuery);
        } else {
            message = queryByQuestion(buttonQuery, bot);
        }
        return message;
    }

    private BotApiMethod<?> queryByQuestion(CallbackQuery buttonQuery, TelegramWebHook bot) throws TelegramApiException, IOException {
        Question question = dataCache.getCurrentQuestion(userId);
        ActionType actionType = getActionType(buttonQuery, bot, question);
        if (actionType == null) return null;
        if (actionType == ActionType.CALENDAR) {
            return regexCalendar(buttonQuery, question);
        }
        return null;
    }

    private ActionType getActionType(CallbackQuery buttonQuery, TelegramWebHook bot, Question question) throws TelegramApiException {
        ActionType actionType;
        try {
            actionType = question.getActions().stream()
                    .findFirst()
                    .orElseThrow(RuntimeException::new)
                    .getType();
        } catch (Exception e) {
            bot.execute(
                    messageService.deleteMessage(
                            chatId,
                            buttonQuery.getMessage().getMessageId()
                    ));
            return null;
        }
        return actionType;
    }

    private BotApiMethod<?> regexCalendar(CallbackQuery buttonQuery, Question question) throws TelegramApiException, IOException {
        if (buttonQuery.getData().equals(CalendarUtil.IGNORE))
            return null;
        else if (Pattern.matches(question.getRegex(), buttonQuery.getData())) {
            return setCalendarAnswer(buttonQuery);
        } else {
            return messageService.updateCalendar(buttonQuery.getMessage(),
                    langId,
                    LocalDate.parse(buttonQuery.getData() + "-" + LocalDate.now().getDayOfMonth())
            );
        }
    }

    private SendMessage setCalendarAnswer(CallbackQuery buttonQuery) throws TelegramApiException, IOException {
        LocalDate date = LocalDate.parse(buttonQuery.getData());
        if (date.isBefore(LocalDate.now())) {
            return messageService.createError(chatId, new BeforeCurrentDateException(), langId);
        }
        setActionTypeAnswered();
        buttonQuery.getMessage().setText(buttonQuery.getData());
        buttonQuery.getMessage().setFrom(buttonQuery.getFrom());
        return inputMessageHandler.handle(buttonQuery.getMessage(), bot, false);
    }

    private void setActionTypeAnswered() {
        Question currentQuestion = dataCache.getCurrentQuestion(userId);
        currentQuestion.getActions().stream()
                .findFirst()
                .orElseThrow(RuntimeException::new).setType(ActionType.CALENDAR_ANSWER);
        dataCache.setQuestion(userId, currentQuestion);
    }

    private BotApiMethod<?> regexInlineButton(CallbackQuery buttonQuery) throws TelegramApiException, IOException {
        if (Objects.equals(buttonQuery.getData(), QueryType.NEXT.toString())) {
            listenerService.sendNextPhotos(userId, this.bot);
            return messageService.deleteMessage(chatId, buttonQuery.getMessage().getMessageId());
        }
        return null;
    }

    /**
     * Method sets data that is used in other methods.
     *
     * @param query user response query
     * @param bot   TelegramWebHook for sending additional messages
     */
    private void setData(CallbackQuery query, TelegramWebHook bot) {
        this.userId = query.getFrom().getId();
        this.chatId = query.getMessage().getChatId().toString();
        this.langId = dataCache.getUserData(userId).getLangId();
        this.bot = bot;
    }

}
