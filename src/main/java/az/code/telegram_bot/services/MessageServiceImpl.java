package az.code.telegram_bot.services;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.models.Action;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.utils.ButtonsUtil;
import az.code.telegram_bot.utils.CalendarUtil;
import az.code.telegram_bot.utils.TranslateUtil;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Set;

@Service
public class MessageServiceImpl implements MessageService {
    final
    ButtonsUtil buttonsUtil;

    final
    CalendarUtil calendarUtil;

    final
    TranslateUtil translateUtil;

    public MessageServiceImpl(ButtonsUtil buttonsUtil, CalendarUtil calendarUtil, TranslateUtil translateUtil) {
        this.buttonsUtil = buttonsUtil;
        this.calendarUtil = calendarUtil;
        this.translateUtil = translateUtil;
    }

    @Override
    public String questionGenerator(Question question, Long langId) {
        return translateUtil.getQuestionTranslate(question, langId);
    }

    @Override
    public SendMessage simpleQuestionMessage(String chatId, Question question, Long langId) {
        SendMessage sendMessage = new SendMessage(chatId, questionGenerator(question, langId));
        sendMessage.setReplyMarkup(buttonsUtil.removeReplyKeyboard());
        return sendMessage;
    }

    @Override
    public SendMessage createCalendar(String chatId, Question question, Long langId) {
        return buttonsUtil.buttonMessage(
                calendarUtil.generateKeyboard(LocalDate.now()),
                chatId,
                questionGenerator(question, langId)
        );
    }

    @Override
    public SendMessage msgWithInlKeyboard(String chatId, Question question, Long langId) {
        return buttonsUtil.buttonMessage(
                createInlMarkup(question.getActions(), langId),
                chatId,
                questionGenerator(question, langId)
        );
    }

    @Override
    public SendMessage msgWithRepKeyboard(String chatId, Question question, Long langId) {
        return buttonsUtil.buttonMessage(
                createRepMarkup(question.getActions(), langId),
                chatId,
                questionGenerator(question, langId)
        );
    }

    @Override
    public void sendData(String chatId, Long userId, String data, TelegramWebHook bot) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(chatId, data);
        sendMessage.setReplyMarkup(buttonsUtil.removeReplyKeyboard());
        bot.execute(sendMessage);
    }

    private ReplyKeyboardMarkup createRepMarkup(Set<Action> actions, Long langId) {
        List<KeyboardRow> keyboard = buttonsUtil.createRepKeyboard(
                translateUtil.getActionsTranslate(actions, langId)
        );
        return new ReplyKeyboardMarkup(keyboard, true, false, true, "");
    }

    private InlineKeyboardMarkup createInlMarkup(Set<Action> actions, Long langId) {
        List<List<InlineKeyboardButton>> keyboard = buttonsUtil.createInlKeyboard(
                translateUtil.getActionsTranslate(actions, langId)
        );
        return new InlineKeyboardMarkup(keyboard);
    }


}
