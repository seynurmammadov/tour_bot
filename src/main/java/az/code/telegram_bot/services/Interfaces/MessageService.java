package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.models.Action;
import az.code.telegram_bot.models.Question;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Set;

public interface MessageService {
    SendMessage simpleMessage(String chatId, Question question, Long langId);

    SendMessage createCalendar(String chatId, Question question, Long langId);

    String questionGenerator(Question question, Long langId);

    SendMessage msgWithRepKeyboard(String chatId, Question question, Long langId);

    SendMessage msgWithInlKeyboard(String chatId, Question question, Long langId);

    AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery);
}
