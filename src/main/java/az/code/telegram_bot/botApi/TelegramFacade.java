package az.code.telegram_bot.botApi;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.MessageHandler;
import az.code.telegram_bot.botApi.handlers.QueryHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.utils.LogUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramFacade {
    final
    DataCache dataCache;
    final
    MessageHandler inputMessageHandler;
    final
    QueryHandler callbackQueryHandler;
    final
    LogUtil logUtil;

    public TelegramFacade(DataCache dataCache,
                          MessageHandler inputMessageHandler,
                          QueryHandler callbackQueryHandler, LogUtil logUtil) {
        this.dataCache = dataCache;
        this.inputMessageHandler = inputMessageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.logUtil = logUtil;
    }

    public BotApiMethod<?> handleUpdate(Update update, TelegramWebHook bot) throws TelegramApiException {
        SendMessage replyMessage = null;
        Message message = update.getMessage();
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            logUtil.logCallBackQuery(update, callbackQuery);
            return processCallbackQuery(callbackQuery);
        }
        if (message != null && message.hasText()) {
            logUtil.logNewMessage(message);
            replyMessage = handleInputMessage(message, bot);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message, TelegramWebHook bot) throws TelegramApiException {
        String chatId = message.getChatId().toString();
        Question state = dataCache.getState(message.getFrom().getId());
        return inputMessageHandler.handle(message, state, chatId, bot);
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final String chatId = buttonQuery.getMessage().getChatId().toString();
        final long userId = buttonQuery.getFrom().getId();
        return callbackQueryHandler.handle(buttonQuery, chatId, userId);
    }

}