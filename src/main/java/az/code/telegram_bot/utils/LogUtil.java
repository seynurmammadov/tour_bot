package az.code.telegram_bot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class LogUtil {
    public void logNewMessage(Message message) {
        log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                message.getFrom().getUserName(),
                message.getFrom().getId(),
                message.getChatId(),
                message.getText());
    }

    public void logCallBackQuery(Update update, CallbackQuery callbackQuery) {
        log.info("New callbackQuery from User: {}, userId: {}, with data: {}",
                update.getCallbackQuery().getFrom().getUserName(),
                callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
    }
}
