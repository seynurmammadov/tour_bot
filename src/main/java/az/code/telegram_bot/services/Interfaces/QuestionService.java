package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.enums.BotState;
import org.springframework.data.jpa.repository.Query;


public interface QuestionService {
    @Query("select q from Question q where q.keyword=:botState")
    Question getQuestionByKeyword(BotState botState);
}
