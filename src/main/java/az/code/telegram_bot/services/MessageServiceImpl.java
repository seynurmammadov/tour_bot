package az.code.telegram_bot.services;

import az.code.telegram_bot.exceptions.MyCustomException;
import az.code.telegram_bot.models.Action;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.BotSession;
import az.code.telegram_bot.models.enums.ActionType;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.utils.ButtonsUtil;
import az.code.telegram_bot.utils.CalendarUtil;
import az.code.telegram_bot.utils.TranslateUtil;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import java.util.ArrayList;
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
    public SendMessage createNextBtn(BotSession botSession, Question nextQuestion, Long langId) {
        return buttonsUtil.buttonMessage(
                createInlMarkup(nextQuestion.getActions(), langId),
                botSession.getChatId(),
                getNextBtnText(botSession, nextQuestion, langId)
        );
    }

    @Override
    public EditMessageText updateNextBtn(BotSession botSession, Question nextQuestion, Long langId) {
        return EditMessageText.builder()
                .chatId(botSession.getChatId())
                .messageId(botSession.getNextMessageId())
                .text(getNextBtnText(botSession, nextQuestion, langId))
                .replyMarkup(createInlMarkup(nextQuestion.getActions(), langId))
                .build();
    }
    public String getNextBtnText(BotSession botSession, Question nextQuestion, Long langId) {
        return String.format(
                questionGenerator(nextQuestion, langId),
                botSession.getCountOfOffers() - botSession.getCountOfSent()
        );
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
    public EditMessageReplyMarkup updateCalendar(Message message, Long langId, LocalDate localDate) {
        return EditMessageReplyMarkup.builder()
                .chatId(message.getChatId().toString())
                .replyMarkup(calendarUtil.generateCalendar(localDate, langId))
                .messageId(message.getMessageId())
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
    public SendMessage createMsgWithData(String chatId, Long userId, String data) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(data)
                .replyMarkup(buttonsUtil.removeReplyKeyboard())
                .build();
    }

    @Override
    public SendPhoto createPhoto(String chatId, InputFile inputFile) {
        return SendPhoto.builder()
                .chatId(chatId)
                .photo(inputFile)
                .build();
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
    public EditMessageText editCalendarMessage(Question question, Message message, Long langId) {
        return EditMessageText.builder()
                .chatId(message.getChatId().toString())
                .text(
                        questionGenerator(question, langId) + "\n" + message.getText()
                )
                .messageId(message.getMessageId())
                .replyMarkup(new InlineKeyboardMarkup(new ArrayList<>()))
                .build();
    }

    @Override
    public DeleteMessage deleteMessage(String chatId, Integer messageId) {
        return DeleteMessage.builder().chatId(chatId).messageId(messageId).build();
    }
    @Override
    public SendMessage getMessageByAction(Question question, long langId,
                                          ActionType actionType,String chatId) {
        switch (actionType) {
            case FREETEXT:
                return simpleQuestionMessage(chatId, question, langId);
            case BUTTON:
                return msgWithRepKeyboard(chatId, question, langId);
            case INLINE_BUTTON:
                return msgWithInlKeyboard(chatId, question, langId);
            case CALENDAR:
                return createCalendar(chatId, question, langId);
            case BUTTON_CONTACT_INFO:
                return createBtnContactInfo(chatId, question, langId);
            default:
                return null;
        }
    }
    private SendMessage createBtnContactInfo(String chatId, Question question, Long langId) {
        return buttonsUtil.buttonMessage(
                createRepMarkup(question.getActions(), langId,true),
                chatId,
                questionGenerator(question, langId)
        );
    }
    private ReplyKeyboardMarkup createRepMarkup(Set<Action> actions, Long langId,boolean contact_info) {
        return ReplyKeyboardMarkup.builder()
                .keyboard(
                        buttonsUtil.createRepKeyboard(
                                translateUtil.getActionsTranslate(actions, langId),contact_info
                        )
                )
                .resizeKeyboard(true)
                .selective(true)
                .oneTimeKeyboard(false)
                .build();
    }
    private ReplyKeyboardMarkup createRepMarkup(Set<Action> actions, Long langId) {
        return createRepMarkup(actions,langId,false);
    }

    private InlineKeyboardMarkup createInlMarkup(Set<Action> actions, Long langId) {
        return new InlineKeyboardMarkup(
                buttonsUtil.createInlKeyboard(
                        translateUtil.getActionsTranslate(actions, langId)
                )
        );
    }
}
