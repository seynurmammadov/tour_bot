package az.code.telegram_bot.controllers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.enums.BotState;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@Slf4j
public class WebHookController {
    private final TelegramWebHook telegramBot;
    final
    QuestionService questionService;

    public WebHookController(TelegramWebHook telegramBot, QuestionService questionService) {
        this.telegramBot = telegramBot;
        this.questionService = questionService;
    }

    @RequestMapping(value = "callback/webhook", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }


}
