package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.exceptions.*;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.UserData;
import az.code.telegram_bot.models.enums.CommandType;
import az.code.telegram_bot.repositories.RedisRepository;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.TourRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Queue;

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


    public CommandHandler(MessageService messageService, DataCache dataCache, MessageHandler inputMessageHandler, TourRequestService tourService) {
        this.messageService = messageService;
        this.dataCache = dataCache;
        this.inputMessageHandler = inputMessageHandler;
        this.tourService = tourService;
    }

    @Override
    public SendMessage handle(Message message, TelegramWebHook bot) throws TelegramApiException {
        setData(message, bot);
        CommandType commandType = CommandType.valueOfCommand(message.getText());

        switch (commandType) {
            case START:
                return startCommand(message, bot);
            case STOP:
                return stopCommand();
            default:
                return messageService.createError(chatId,
                        new UnknownCommandException(),
                        dataCache.getUserProfileData(userId).getLangId());
        }
    }

    private SendMessage stopCommand() {

        Question question =dataCache.getCurrentQuestion(userId);
        if(question!=null ){
            dataCache.clearDataAndState(userId);
            tourService.deactiveSeance(userId);
            return messageService.createNotify(chatId,
                    new StopNotifyException(),
                    dataCache.getUserProfileData(userId).getLangId());
        }
        else  {
            return messageService.createError(chatId,
                    new StartBeforeStopException(),
                    dataCache.getUserProfileData(userId).getLangId());
        }
    }

    private SendMessage startCommand(Message message, TelegramWebHook bot) throws TelegramApiException {
        if (dataCache.setFirstQuestion(userId)) {
            tourService.createSeance(userId, chatId);
            return inputMessageHandler.handle(message, bot);
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
