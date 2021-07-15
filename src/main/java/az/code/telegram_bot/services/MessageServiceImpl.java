package az.code.telegram_bot.services;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.exceptions.MyCustomException;
import az.code.telegram_bot.models.Action;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.BotSession;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.utils.ButtonsUtil;
import az.code.telegram_bot.utils.CalendarUtil;
import az.code.telegram_bot.utils.TranslateUtil;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
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

    //TODO botu cole cixartmaq
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
    public Message sendNextButton(TelegramWebHook bot, BotSession botSession, Question nextQuestion, Long langId) throws TelegramApiException {
        String text = String.format(questionGenerator(nextQuestion, langId),
                botSession.getCountOfOffers() - botSession.getCountOfSent());
        return bot.execute(buttonsUtil.buttonMessage(
                createInlMarkup(nextQuestion.getActions(), langId),
                botSession.getChatId(),
                text
        ));
    }

    @Override
    public void updateNextButton(TelegramWebHook bot, BotSession botSession, Question nextQuestion, Long langId) throws TelegramApiException {
        String text = String.format(questionGenerator(nextQuestion, langId),
                botSession.getCountOfOffers() - botSession.getCountOfSent());
        bot.execute(EditMessageText.builder()
                .chatId(botSession.getChatId())
                .messageId(botSession.getNextMessageId())
                .text(text)
                .replyMarkup(createInlMarkup(nextQuestion.getActions(), langId))
                .build());
    }

    @Override
    public SendMessage simpleQuestionMessage(String chatId, Question question, Long langId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(questionGenerator(question, langId))
                .replyMarkup(buttonsUtil.removeReplyKeyboard())
                .build();
    }


    @Override
    public SendMessage createCalendar(String chatId, Question question, Long langId) {
        return buttonsUtil.buttonMessage(
                calendarUtil.generateCalendar(LocalDate.now(), langId),
                chatId,
                questionGenerator(question, langId)
        );
    }

    @Override
    public EditMessageReplyMarkup updateCalendar(String chatId, Question question, Long langId, Integer messageId, LocalDate localDate) {
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .replyMarkup(calendarUtil.generateCalendar(localDate, langId))
                .messageId(messageId)
                .build();

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
        bot.execute(SendMessage.builder()
                .chatId(chatId)
                .text(data)
                .replyMarkup(buttonsUtil.removeReplyKeyboard())
                .build());
    }

    @Override
    public SendMessage createError(String chatId, MyCustomException exception, Long langId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(exception.getLocalizedMessage(langId))
                .build();
    }

    @Override
    public SendMessage createNotify(String chatId, MyCustomException exception, Long langId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(exception.getLocalizedMessage(langId))
                .replyMarkup(buttonsUtil.removeReplyKeyboard())
                .build();
    }

    @Override
    public EditMessageText editInlineKeyboardText(String chatId, Question question, Message message, Long langId) {
        return EditMessageText.builder()
                .chatId(chatId)
                .text(questionGenerator(question, langId) + "\n" + message.getText())
                .messageId(message.getMessageId())
                .replyMarkup(new InlineKeyboardMarkup(new ArrayList<>()))
                .build();
    }

    @Override
    public DeleteMessage deleteMessage(String chatId, Integer messageId) {
        return DeleteMessage.builder().chatId(chatId).messageId(messageId).build();
    }

    private ReplyKeyboardMarkup createRepMarkup(Set<Action> actions, Long langId) {
        List<KeyboardRow> keyboard = buttonsUtil.createRepKeyboard(
                translateUtil.getActionsTranslate(actions, langId)
        );
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboard)
                .resizeKeyboard(true)
                .selective(true)
                .oneTimeKeyboard(false)
                .build();
    }

    private InlineKeyboardMarkup createInlMarkup(Set<Action> actions, Long langId) {
        List<List<InlineKeyboardButton>> keyboard = buttonsUtil.createInlKeyboard(
                translateUtil.getActionsTranslate(actions, langId));
        return new InlineKeyboardMarkup(keyboard);
    }
}
