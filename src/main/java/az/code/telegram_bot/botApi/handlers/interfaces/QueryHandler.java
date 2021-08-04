package az.code.telegram_bot.botApi.handlers.interfaces;

import az.code.telegram_bot.TelegramWebHook;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface QueryHandler {
    BotApiMethod<?> handle(CallbackQuery buttonQuery, TelegramWebHook bot) throws TelegramApiException, IOException;
}