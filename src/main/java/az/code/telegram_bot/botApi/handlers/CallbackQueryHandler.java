package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.cache.DataCacheImpl;
import az.code.telegram_bot.models.Question;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackQueryHandler implements QueryHandler {
    final
    DataCacheImpl dataCache;

    public CallbackQueryHandler(DataCacheImpl dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery buttonQuery, String chatId, Long userId) {
        SendMessage callBackAnswer = new SendMessage();
        Question state = dataCache.getCurrentQuestion(userId);
    /*    switch (state.getState()){
            case "LANGUAGE":
                EditMessageText markup = new EditMessageText();
                markup.setChatId(chatId);
                markup.setInlineMessageId(buttonQuery.getInlineMessageId());
                markup.setReplyMarkup(new InlineKeyboardMarkup());
                markup.setMessageId(buttonQuery.getMessage().getMessageId());
                markup.setText("\uD83C\uDF0E Selected language \uD83C\uDF0E \n "+state.getQuestionTranslates().stream()
                        .map(QuestionTranslate::getLanguage).filter(s->s.getId().equals(Long.parseLong(buttonQuery.getData())))
                        .findFirst().get().getLang());
                return markup;
            case "CALENDAR":
            default:
        }*/

        return callBackAnswer;
    }



}
