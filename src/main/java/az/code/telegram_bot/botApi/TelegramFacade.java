package az.code.telegram_bot.botApi;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.botApi.handlers.interfaces.QueryHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.AgencyOffer;
import az.code.telegram_bot.services.Interfaces.ListenerService;
import az.code.telegram_bot.utils.LogUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

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
    final
    MessageHandler commandHandler;
    final
    ListenerService listenerService;
    final
    MessageHandler replyHandler;

    public TelegramFacade(DataCache dataCache,
                          @Qualifier("inputMessageHandler") MessageHandler inputMessageHandler,
                          QueryHandler callbackQueryHandler, LogUtil logUtil,
                          @Qualifier("commandHandler") MessageHandler commandHandler,
                          ListenerService listenerService,
                          @Qualifier("replyMessageHandler") MessageHandler replyHandler) {
        this.dataCache = dataCache;
        this.inputMessageHandler = inputMessageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.logUtil = logUtil;
        this.commandHandler = commandHandler;
        this.listenerService = listenerService;
        this.replyHandler = replyHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update, TelegramWebHook bot) throws TelegramApiException, IOException {
        Message message = update.getMessage();
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            logUtil.logCallBackQuery(update, callbackQuery);
            return callbackQueryHandler.handle(callbackQuery, bot);
        } else if(message!=null){
            return inlineMessage(bot, message);
        }
        return null;
    }

    private SendMessage inlineMessage(TelegramWebHook bot, Message message) throws TelegramApiException, IOException {
        if (message.getReplyToMessage() != null) {
            logUtil.logNewMessage(message, "reply");
            return replyHandler.handle(message, bot, true);
        } else if (isCommand(message)) {
            logUtil.logNewMessage(message, "command");
            return commandHandler.handle(message, bot, true);
        } else if (message.hasText()) {
            logUtil.logNewMessage(message);
            return inputMessageHandler.handle(message, bot, false);
        }
        return null;
    }

    public void sendPhoto(AgencyOffer agencyOffer, TelegramWebHook bot) throws IOException, TelegramApiException {
        listenerService.sendPhoto(agencyOffer, bot);
    }

    private boolean isCommand(Message message) {
        if (message.hasText()) {
            return message.getText().startsWith("/");
        }
        return false;
    }

}