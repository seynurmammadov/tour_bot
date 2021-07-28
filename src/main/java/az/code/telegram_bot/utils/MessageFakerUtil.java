package az.code.telegram_bot.utils;

import az.code.telegram_bot.models.BotSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class MessageFakerUtil {
    public Message fakeStop(BotSession botSession, String chatId) {
        Message message = new Message();
        User user = new User();
        user.setId(botSession.getClient_id());
        Chat chat = new Chat();
        chat.setId(Long.valueOf(chatId));
        message.setText("/stop");
        message.setFrom(user);
        message.setChat(chat);
        return message;
    }
}
