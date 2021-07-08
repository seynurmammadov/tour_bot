package az.code.telegram_bot;

import az.code.telegram_bot.botApi.TelegramFacade;
import lombok.Builder;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Builder
public class TelegramWebHook extends TelegramWebhookBot {
    private final String webHookPath;
    private final String botUsername;
    private final String botToken;
    private final String botPath;
    private final TelegramFacade telegramFacade;


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

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update,this);
    }
}
