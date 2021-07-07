package az.code.telegram_bot.utils;

import az.code.telegram_bot.models.ActionTranslate;
import org.springframework.stereotype.Component;
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
            btn.setCallbackData(text.getId().toString());
            btn.setText(text.getContext());
            rowInline.add(btn);
            rowsInline.add(rowInline);
        }
        return rowsInline;
    }
}
