package az.code.telegram_bot.botApi.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
@Component
public class StartCommand extends BotCommand implements IBotCommand{

//TODO remove hard code there
    public StartCommand() {
        super("start", "With this command you can start the Bot");
    }

}
