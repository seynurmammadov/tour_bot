package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.exceptions.StopBeforeException;
import az.code.telegram_bot.exceptions.UnknownCommandException;
import az.code.telegram_bot.models.enums.CommandType;
import az.code.telegram_bot.services.Interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class CommandHandler implements MessageHandler {
    final
    MessageService messageService;
    final
    DataCache dataCache;
    final
    MessageHandler inputMessageHandler;

    private Long userId;
    private String chatId;
    private TelegramWebHook bot;

    public CommandHandler(MessageService messageService, DataCache dataCache, MessageHandler inputMessageHandler) {
        this.messageService = messageService;
        this.dataCache = dataCache;
        this.inputMessageHandler = inputMessageHandler;
    }

    @Override
    public SendMessage handle(Message message, TelegramWebHook bot) throws TelegramApiException {
        setData(message, bot);
        CommandType commandType = CommandType.valueOfCommand(message.getText());

        switch (commandType) {
            case START:
                if(dataCache.setFirstQuestion(userId)){
                    return inputMessageHandler.handle(message, bot);
                }
                else {
                    return messageService.createError(chatId,
                            new StopBeforeException(),
                            dataCache.getUserProfileData(userId).getLangId());
                }
            case STOP:
                return new SendMessage(message.getChatId().toString(), "STOP");
            default:
                return messageService.createError(chatId,
                        new UnknownCommandException(),
                        dataCache.getUserProfileData(userId).getLangId());
        }
    }

    /**
     * Method sets data that is used in other methods.
     *
     * @param message user response message
     * @param bot     TelegramWebHook for sending additional messages
     */
    private void setData(Message message, TelegramWebHook bot) {
        this.userId = message.getFrom().getId();
        this.chatId = message.getChatId().toString();
        this.bot = bot;
    }

}
