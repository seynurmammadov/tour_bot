package az.code.telegram_bot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ReplyMessageService {

    public SendMessage getReplyMessage(String chatId, String replyMessage) {
        return new SendMessage(chatId,replyMessage);
    }

}
