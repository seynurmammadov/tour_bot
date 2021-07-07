package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.enums.BotState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("select q from Question q where q.keyword =:keyword ")
    Question getQuestionByKeyword(BotState keyword);
}
