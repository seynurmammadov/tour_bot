package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.exceptions.MyCustomException;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.BotSession;
import az.code.telegram_bot.models.enums.ActionType;
import org.joda.time.LocalDate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public interface MessageService {
    SendMessage createNextBtn(BotSession botSession, Question nextQuestion, Long langId);

    EditMessageText updateNextBtn(BotSession botSession, Question nextQuestion, Long langId);

    SendMessage simpleQuestionMessage(String chatId, Question question, Long langId);

    SendMessage createCalendar(String chatId, Question question, Long langId);

    EditMessageReplyMarkup updateCalendar(Message message, Long langId, LocalDate localDate);

    String questionGenerator(Question question, Long langId);

    SendMessage msgWithRepKeyboard(String chatId, Question question, Long langId);

    SendMessage msgWithInlKeyboard(String chatId, Question question, Long langId);

    SendMessage createMsgWithData(String chatId, Long userId, String data);

    SendPhoto createPhoto(String chatId, InputFile inputFile);

    SendMessage createError(String chatId, MyCustomException exception, Long langId);

    SendMessage createNotify(String chatId, MyCustomException exception, Long userId);

    DeleteMessage deleteMessage(String chatId, Integer messageId);

    EditMessageText editCalendarMessage(Question question, Message message, Long langId);

    SendMessage getMessageByAction(Question question, long langId, ActionType actionType, String chatId);
}
