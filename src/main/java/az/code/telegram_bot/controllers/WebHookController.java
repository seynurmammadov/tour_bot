package az.code.telegram_bot.controllers;

import az.code.telegram_bot.TelegramWebHook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@RestController
@Slf4j
public class WebHookController {
    private final TelegramWebHook telegramBot;

    public WebHookController(TelegramWebHook telegramBot) {
        this.telegramBot = telegramBot;
    }


    @RequestMapping(value = "send/message/{UUID}", method = RequestMethod.POST)
    public ResponseEntity receivedFromAgent(@RequestParam("file") MultipartFile file, @PathVariable("UUID") String UUID) throws TelegramApiException, IOException {
        return new ResponseEntity(telegramBot.sendPhoto(file,UUID));
    }

    @RequestMapping(value = "callback/webhook", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }


}
