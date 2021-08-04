package az.code.telegram_bot.botApi.handlers.interfaces;

import az.code.telegram_bot.TelegramWebHook;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface MessageHandler {
    SendMessage handle(Message message, TelegramWebHook bot, boolean isCommand) throws TelegramApiException, IOException;
}
