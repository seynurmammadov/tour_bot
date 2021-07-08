package az.code.telegram_bot.utils;

import az.code.telegram_bot.models.Action;
import az.code.telegram_bot.models.ActionTranslate;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.QuestionTranslate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TranslateUtil {
    public List<ActionTranslate> getActionsTranslate(Set<Action> actions, Long langId) {
        return actions.stream()
                .flatMap(a -> a.getActionTranslates().stream()
                        .filter(q -> Objects.equals(q.getLanguage().getId(), langId)))
                .collect(Collectors.toList());
    }
    public String getQuestionTranslate(Question question,Long langId){
       return question.getQuestionTranslates()
                .stream()
                .filter(q -> Objects.equals(q.getLanguage().getId(), langId))
                .map(QuestionTranslate::getContext)
                .collect(Collectors.joining("\n"));
    }
}
