package az.code.telegram_bot.utils;

import az.code.telegram_bot.models.ActionTranslate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class ButtonsUtil {
    public List<KeyboardRow> createRepKeyboard(List<ActionTranslate> actionTranslates) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (ActionTranslate text : actionTranslates) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(text.getContext()));
            keyboard.add(row);
        }
        return keyboard;
    }

    public List<List<InlineKeyboardButton>> createInlKeyboard(List<ActionTranslate> actionTranslates) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (ActionTranslate text : actionTranslates) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setCallbackData(text.getAction().getCallback());
            btn.setText(text.getContext());
            rowInline.add(btn);
            rowsInline.add(rowInline);
        }
        return rowsInline;
    }

    public SendMessage buttonMessage(ReplyKeyboard markup, String chatId, String question) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(question);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public ReplyKeyboardRemove removeReplyKeyboard() {
        ReplyKeyboardRemove remove = new ReplyKeyboardRemove();
        remove.setRemoveKeyboard(true);
        return remove;
    }
}
