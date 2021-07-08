package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("select q from Question q where q.state =:state ")
    Question getQuestionByKeyword(String state);
    @Query("select q from Question q where q.state is null and size(q.actions)>0 ")
    Question getFirstQuestion();
}
