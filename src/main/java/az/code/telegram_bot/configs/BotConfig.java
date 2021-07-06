package az.code.telegram_bot.configs;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;
    private String botPath;

    @Bean
    public TelegramWebHook telegramBot(TelegramFacade telegramFacade) throws TelegramApiException {
        TelegramWebHook telegramBot = TelegramWebHook.builder()
                                                     .botPath(botPath)
                                                     .botUsername(botUserName)
                                                     .webHookPath(webHookPath)
                                                     .botToken(botToken)
                                                     .telegramFacade(telegramFacade)
                                                     .build();
        telegramBot.setWebhook(new SetWebhook(webHookPath));
        return telegramBot;
    }

}
