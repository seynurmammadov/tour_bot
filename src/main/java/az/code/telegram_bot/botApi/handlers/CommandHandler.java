package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.exceptions.*;
import az.code.telegram_bot.models.enums.CommandType;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.TourRequestService;
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
    final
    TourRequestService tourService;


    public CommandHandler(MessageService messageService, DataCache dataCache,
                          MessageHandler inputMessageHandler, TourRequestService tourService) {
        this.messageService = messageService;
        this.dataCache = dataCache;
        this.inputMessageHandler = inputMessageHandler;
        this.tourService = tourService;
    }

    @Override
    public SendMessage handle(Message message, TelegramWebHook bot, boolean isCommand) throws TelegramApiException {
        return handle(message, bot);
    }

    private SendMessage handle(Message message, TelegramWebHook bot) throws TelegramApiException {
        setData(message, bot);
        CommandType commandType = CommandType.valueOfCommand(message.getText());
        switch (commandType) {
            case START:
                return startCommand(message);
            case STOP:
                return stopCommand();
            default:
                return messageService.createError(chatId,
                        new UnknownCommandException(),
                        dataCache.getUserProfileData(userId).getLangId());
        }
    }

    private SendMessage stopCommand() {

        if (tourService.getByUserId(userId).isPresent()) {
            dataCache.clearDataAndState(userId);
            tourService.deactiveSeance(userId);
            return messageService.createNotify(chatId,
                    new StopNotifyException(),
                    dataCache.getUserProfileData(userId).getLangId());
        } else {
            return messageService.createError(chatId,
                    new StartBeforeStopException(),
                    dataCache.getUserProfileData(userId).getLangId());
        }
    }

    private SendMessage startCommand(Message message) throws TelegramApiException {
        if (tourService.getByUserId(userId).isEmpty()) {
            dataCache.setPrimaryQuestion(userId);
            tourService.createSeance(userId, chatId);
            return inputMessageHandler.handle(message, this.bot, true);
        } else {
            return messageService.createError(chatId,
                    new StopBeforeException(),
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
