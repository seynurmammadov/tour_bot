package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.models.Question;

public interface QuestionService {
    Question getByKeyword(String keyword);
    Question getFirst();
}
