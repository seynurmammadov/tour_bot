package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.enums.BotState;

public interface QuestionService {
    Question getQuestionByKeyword(BotState botState);
}
