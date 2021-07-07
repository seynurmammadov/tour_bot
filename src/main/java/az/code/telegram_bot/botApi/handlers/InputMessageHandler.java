package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.models.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface InputMessageHandler {
    SendMessage handle(Message message,BotState botState,Long userId);

    BotState getHandlerName();
}
