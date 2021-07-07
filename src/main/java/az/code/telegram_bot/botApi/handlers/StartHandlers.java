package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.botApi.handlers.InputMessageHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.models.enums.BotState;
import az.code.telegram_bot.services.LanguageService;
import az.code.telegram_bot.services.ReplyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class StartHandlers implements InputMessageHandler {
    private final LanguageService mainMenuService;

    public StartHandlers(LanguageService mainMenuService) {
        this.mainMenuService = mainMenuService;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.LANGUAGE;
    }

    @Override
    public SendMessage handle(Message message,BotState botState) {

        SendMessage sendMessage = mainMenuService.getLangMenu(message.getChatId().toString(), botState.name() );
        return sendMessage;
    }
}
