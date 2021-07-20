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
        return createRepKeyboard(actionTranslates, false);
    }

    public List<KeyboardRow> createRepKeyboard(List<ActionTranslate> actionTranslates, boolean contact_info) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (ActionTranslate text : actionTranslates) {
            KeyboardRow row = new KeyboardRow();
            row.add(KeyboardButton.builder().text(text.getContext()).requestContact(contact_info).build());
            keyboard.add(row);
        }
        return keyboard;
    }

    public List<List<InlineKeyboardButton>> createInlKeyboard(List<ActionTranslate> actionTranslates) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (ActionTranslate text : actionTranslates) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(
                    InlineKeyboardButton.builder()
                            .callbackData(text.getAction().getCallback())
                            .text(text.getContext())
                            .build()
            );
            rowsInline.add(rowInline);
        }
        return rowsInline;
    }

    public SendMessage buttonMessage(ReplyKeyboard markup, String chatId, String question) {
        return SendMessage.builder().replyMarkup(markup).chatId(chatId).text(question).build();
    }

    public ReplyKeyboardRemove removeReplyKeyboard() {
        return ReplyKeyboardRemove.builder().removeKeyboard(true).build();
    }

}
