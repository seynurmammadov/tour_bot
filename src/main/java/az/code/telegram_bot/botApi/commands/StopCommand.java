package az.code.telegram_bot.botApi.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Component
public class StopCommand extends BotCommand implements IBotCommand {
    public StopCommand() {
        super("stop", "With this command you can stop the Bot");
    }
}