package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.models.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface QueryHandler {
    BotApiMethod<?> handle(CallbackQuery buttonQuery, String chatId, Long userId);
}