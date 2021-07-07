package az.code.telegram_bot.botApi;

import az.code.telegram_bot.botApi.handlers.MessageHandler;
import az.code.telegram_bot.botApi.handlers.QueryHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.enums.BotState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {
    final
    DataCache dataCache;

    final
    MessageHandler inputMessageHandler;
    final
    QueryHandler callbackQueryHandler;

    public TelegramFacade(DataCache dataCache,
                          MessageHandler inputMessageHandler,
                          QueryHandler callbackQueryHandler) {
        this.dataCache = dataCache;
        this.inputMessageHandler = inputMessageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;
        Message message = update.getMessage();
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }

        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        long userId = message.getFrom().getId();
        BotState botState = dataCache.getBotState(userId);
        dataCache.setBotState(userId, botState);
        return inputMessageHandler.handle(message, botState, userId);
//        String inputMsg = message.getText();
//        switch (inputMsg) {
//            case "/start":
//                botState = BotState.LANGUAGE;
//                break;
//            default:
//                botState = dataCache.getBotState(userId);
//                break;
//        }

    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final String chatId = buttonQuery.getMessage().getChatId().toString();
        final long userId = buttonQuery.getFrom().getId();
        return callbackQueryHandler.handle(buttonQuery, chatId, userId);
    }

}