package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.botApi.handlers.InputMessageHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.enums.BotState;
import az.code.telegram_bot.services.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class StartHandlers implements InputMessageHandler {
    private DataCache dataCache;
    private ReplyMessageService messagesService;

    public StartHandlers(DataCache dataCache,
                             ReplyMessageService messagesService) {
        this.dataCache = dataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.LANGUAGE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long userId = inputMsg.getFrom().getId();
        String chatId = inputMsg.getChatId().toString();
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "");
        dataCache.setBotState(userId, BotState.LANGUAGE);
        return replyToUser;
    }
}
