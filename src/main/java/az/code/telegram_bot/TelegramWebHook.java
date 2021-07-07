package az.code.telegram_bot;

import az.code.telegram_bot.botApi.TelegramFacade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Builder
public class TelegramWebHook extends TelegramWebhookBot {
    private String webHookPath;
    private String botUsername;
    private String botToken;
    private String botPath;
    private TelegramFacade telegramFacade;


    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        BotApiMethod<?> replyMessageToUser = telegramFacade.handleUpdate(update);
        return replyMessageToUser;
    }
}
