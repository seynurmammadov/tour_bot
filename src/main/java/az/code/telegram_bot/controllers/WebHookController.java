package az.code.telegram_bot.controllers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.configs.RabbitMQConfig;
import az.code.telegram_bot.models.AgencyOffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

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
