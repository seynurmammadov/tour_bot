package az.code.telegram_bot.botApi;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.botApi.handlers.interfaces.QueryHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.AgentOffer;
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

    public TelegramFacade(DataCache dataCache,
                          @Qualifier("inputMessageHandler") MessageHandler inputMessageHandler,
                          QueryHandler callbackQueryHandler, LogUtil logUtil,
                          @Qualifier("commandHandler") MessageHandler commandHandler, ListenerService listenerService) {
        this.dataCache = dataCache;
        this.inputMessageHandler = inputMessageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.logUtil = logUtil;
        this.commandHandler = commandHandler;
        this.listenerService = listenerService;
    }

    public BotApiMethod<?> handleUpdate(Update update, TelegramWebHook bot) throws TelegramApiException {
        SendMessage replyMessage = null;
        Message message = update.getMessage();
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            logUtil.logCallBackQuery(update, callbackQuery);
            return processCallbackQuery(callbackQuery);
        }
        else if(isCommand(message)){
            logUtil.logNewMessage(message,"command");
            replyMessage = commandHandler.handle(message,bot,true);
        }
        else if(message != null && message.hasText()) {
            logUtil.logNewMessage(message);
            replyMessage = inputMessageHandler.handle(message, bot,false);
        }
        return replyMessage;
    }
    public void sendPhoto(AgentOffer agentOffer, TelegramWebHook bot) throws IOException, TelegramApiException {
         listenerService.sendPhoto(agentOffer,bot);
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final String chatId = buttonQuery.getMessage().getChatId().toString();
        final long userId = buttonQuery.getFrom().getId();
        return callbackQueryHandler.handle(buttonQuery, chatId, userId);
    }
    private boolean isCommand(Message message){
        return message.getText().startsWith("/");
    }

}