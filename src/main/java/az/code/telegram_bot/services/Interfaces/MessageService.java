package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.exceptions.MyCustomException;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.TourRequest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public interface MessageService {
    Message sendNextButton(TelegramWebHook bot, TourRequest tourRequest, Question nextQuestion, Long langId) throws TelegramApiException;
    void updateNextButton(TelegramWebHook bot, TourRequest tourRequest, Question nextQuestion, Long langId) throws TelegramApiException;

    SendMessage simpleQuestionMessage(String chatId, Question question, Long langId);

    SendMessage createCalendar(String chatId, Question question, Long langId);

    String questionGenerator(Question question, Long langId);

    SendMessage msgWithRepKeyboard(String chatId, Question question, Long langId);

    SendMessage msgWithInlKeyboard(String chatId, Question question, Long langId);

    void sendData(String chatId, Long userId, String data, TelegramWebHook bot) throws TelegramApiException;

    SendMessage createError(String chatId, MyCustomException exception, Long currentLanguage);

    SendMessage createNotify(String chatId, MyCustomException exception, Long currentLanguage);
}
