package az.code.telegram_bot.botApi.handlers.interfaces;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface QueryHandler {
    BotApiMethod<?> handle(CallbackQuery buttonQuery, String chatId, Long userId);
}