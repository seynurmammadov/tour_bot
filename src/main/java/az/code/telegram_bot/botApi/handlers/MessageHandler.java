package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.models.Question;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface MessageHandler {
    SendMessage handle(Message message, Question state,String chatId, TelegramWebHook bot)
                                                                                    throws TelegramApiException;
}
