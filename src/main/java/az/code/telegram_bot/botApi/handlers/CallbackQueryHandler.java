package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.models.enums.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class CallbackQueryHandler implements QueryHandler {
    @Override
    public BotApiMethod<?> handle(CallbackQuery buttonQuery, String chatId, Long userId) {
        SendMessage callBackAnswer = new SendMessage();

        if (buttonQuery.getData().equals("1")) {
            callBackAnswer = new SendMessage(chatId, "Как тебя зовут ?");
            EditMessageText markup = new EditMessageText();
            markup.setChatId(chatId);
            markup.setInlineMessageId(buttonQuery.getInlineMessageId());
            markup.setReplyMarkup(new InlineKeyboardMarkup());
            markup.setMessageId(buttonQuery.getMessage().getMessageId());
            markup.setText("You select Russian language!");
            return markup;
        } else if (buttonQuery.getData().equals("2")) {
            callBackAnswer = new SendMessage(chatId, "Как тебя зовут ?");

        } else if (buttonQuery.getData().equals("3")) {
            callBackAnswer = new SendMessage(chatId, "Как тебя зовут ?");
        }
        return callBackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }

}
