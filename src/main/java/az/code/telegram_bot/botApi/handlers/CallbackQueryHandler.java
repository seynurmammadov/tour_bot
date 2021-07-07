package az.code.telegram_bot.botApi.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class CallbackQueryHandler implements QueryHandler {
    @Override
    public BotApiMethod<?> handle(CallbackQuery buttonQuery, String chatId, Long userId) {
        SendMessage callBackAnswer = new SendMessage();
        if (buttonQuery.getData().equals("1")) {
            EditMessageText markup = new EditMessageText();
            markup.setChatId(chatId);
            markup.setInlineMessageId(buttonQuery.getInlineMessageId());
            markup.setReplyMarkup(new InlineKeyboardMarkup());
            markup.setMessageId(buttonQuery.getMessage().getMessageId());
            markup.setText("You select Russian language!");
            return markup;

        }
        return callBackAnswer;
    }



}
