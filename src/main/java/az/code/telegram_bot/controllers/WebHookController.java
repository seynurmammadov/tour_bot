package az.code.telegram_bot.controllers;

import az.code.telegram_bot.TelegramWebHook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@Slf4j
public class WebHookController {
    private final TelegramWebHook telegramBot;

    public WebHookController(TelegramWebHook telegramBot) {
        this.telegramBot = telegramBot;
    }

    @RequestMapping(value = "callback/webhook", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }

}
