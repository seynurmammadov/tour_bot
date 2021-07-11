package az.code.telegram_bot.configs;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.TelegramFacade;
import az.code.telegram_bot.botApi.commands.StartCommand;
import az.code.telegram_bot.botApi.commands.StopCommand;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;
    private String botPath;

    List<BotCommand> commands;

    public BotConfig() {
        this.commands = new ArrayList<>();
        setCommands();
    }

    @Bean
    public TelegramWebHook telegramBot(TelegramFacade telegramFacade) throws TelegramApiException {
        TelegramWebHook telegramBot = TelegramWebHook.builder()
                .botPath(botPath)
                .botUsername(botUserName)
                .webHookPath(webHookPath)
                .botToken(botToken)
                .telegramFacade(telegramFacade)
                .build();
        telegramBot.execute(SetMyCommands.builder().commands(commands).build());
        telegramBot.setWebhook(new SetWebhook(webHookPath));
        return telegramBot;
    }

    public void setCommands(){
        commands.add(new StartCommand());
        commands.add(new StopCommand());
    }
}
