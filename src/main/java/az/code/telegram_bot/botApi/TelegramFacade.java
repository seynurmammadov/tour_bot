package az.code.telegram_bot.botApi;

import az.code.telegram_bot.botApi.handlers.InputMessageHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.enums.BotState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {
    final
    DataCache dataCache;

    final
    InputMessageHandler inputMessageHandler;

    public TelegramFacade(DataCache dataCache, InputMessageHandler inputMessageHandler) {
        this.dataCache = dataCache;
        this.inputMessageHandler = inputMessageHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;
        Message message = update.getMessage();
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
        return inputMessageHandler.handle(message,botState,userId);
//        String inputMsg = message.getText();
//
//        switch (inputMsg) {
//            case "/start":
//                botState = BotState.LANGUAGE;
//                break;
//            default:
//                botState = dataCache.getBotState(userId);
//                break;
//        }

    }

}