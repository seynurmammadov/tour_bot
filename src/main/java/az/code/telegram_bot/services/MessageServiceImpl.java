package az.code.telegram_bot.services;

import az.code.telegram_bot.models.Action;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.QuestionTranslate;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.utils.ButtonsUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    final
    ButtonsUtil buttonsUtil;

    public MessageServiceImpl(ButtonsUtil buttonsUtil) {
        this.buttonsUtil = buttonsUtil;
    }

    @Override
    public String questionGenerator(Question question, Long langId) {
        if (langId == -1) {
            return question.getQuestionTranslates().stream()
                    .map(QuestionTranslate::getContext)
                    .collect(Collectors.joining("\n"));
        } else {
            return question.getQuestionTranslates()
                    .stream()
                    .filter(q -> Objects.equals(q.getLanguage().getId(), langId))
                    .findFirst()
                    .get()
                    .getContext();
        }
    }

    @Override
    public SendMessage simpleMessage(String chatId, Question question, Long langId) {
        return new SendMessage(chatId, questionGenerator(question, langId));
    }


    @Override
    public SendMessage msgWithInlKeyboard(String chatId, Question question, Long langId) {
        InlineKeyboardMarkup markup = createInlMarkup(question.getActions(),langId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(questionGenerator(question, langId));
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    @Override
    public SendMessage msgWithRepKeyboard(String chatId, Question question, Long langId) {
        ReplyKeyboardMarkup markup = createRepMarkup(question.getActions(), langId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(questionGenerator(question, langId));
        sendMessage.setReplyMarkup(markup);

        return sendMessage;
    }

    private ReplyKeyboardMarkup createRepMarkup(Set<Action> actions, Long langId) {
        List<KeyboardRow> keyboard;
        if (langId != -1) {
            keyboard = buttonsUtil.createRepKeyboard(actions.stream()
                    .flatMap(a -> a.getActionTranslates().stream()
                            .filter(q -> Objects.equals(q.getLanguage().getId(), langId)))
                    .collect(Collectors.toList()));

        } else {
            keyboard = buttonsUtil.createRepKeyboard(actions.stream()
                    .flatMap(a -> a.getActionTranslates().stream())
                    .collect(Collectors.toList()));
        }
        return new ReplyKeyboardMarkup(keyboard, true, true, true, "");
    }

    private InlineKeyboardMarkup createInlMarkup(Set<Action> actions, Long langId){
        List<List<InlineKeyboardButton>> keyboard;
        if (langId != -1) {
            keyboard = buttonsUtil.createInlKeyboard(actions.stream()
                    .flatMap(a -> a.getActionTranslates().stream()
                            .filter(q -> Objects.equals(q.getLanguage().getId(), langId)))
                    .collect(Collectors.toList()));

        } else {
            keyboard = buttonsUtil.createInlKeyboard(actions.stream()
                    .flatMap(a -> a.getActionTranslates().stream())
                    .collect(Collectors.toList()));
        }
        return new InlineKeyboardMarkup(keyboard);
    }
}
