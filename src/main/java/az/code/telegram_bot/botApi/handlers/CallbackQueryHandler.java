package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.QueryHandler;
import az.code.telegram_bot.cache.DataCacheImpl;
import az.code.telegram_bot.services.Interfaces.ListenerService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Objects;

@Component
public class CallbackQueryHandler implements QueryHandler {
    final
    DataCacheImpl dataCache;

    private Long userId;
//    private String chatId;
    private TelegramWebHook bot;

    final
    ListenerService listenerService;

    public CallbackQueryHandler(DataCacheImpl dataCache, ListenerService listenerService) {
        this.dataCache = dataCache;
        this.listenerService = listenerService;
    }

    //TODO add calendar
    @Override
    public BotApiMethod<?> handle(CallbackQuery buttonQuery, TelegramWebHook bot) throws TelegramApiException, IOException {
      /*  Question state = dataCache.getCurrentQuestion(userId);
        switch (state.getState()){
            case "CALENDAR":
                return null;
            default:
        }*/
        setData(buttonQuery, bot);
        //TODO remove hard code
        if (Objects.equals(buttonQuery.getData(), "NEXT")) {
            listenerService.sendNextPhotos(userId,this.bot);
            return DeleteMessage.builder()
                    .messageId(buttonQuery.getMessage().getMessageId())
                    .chatId(buttonQuery.getMessage().getChatId().toString())
                    .build();
        }
        return null;
    }

    /**
     * Method sets data that is used in other methods.
     *
     * @param query  user response query
     * @param bot    TelegramWebHook for sending additional messages
     */
    private void setData(CallbackQuery query, TelegramWebHook bot) {
        this.userId = query.getFrom().getId();
//        this.chatId = query.getMessage().getChatId().toString();
        this.bot = bot;
    }

}
