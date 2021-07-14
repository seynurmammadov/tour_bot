package az.code.telegram_bot.utils;

import az.code.telegram_bot.models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.Assert.assertEquals;


@SpringBootTest
public class TranslateUtilTest {
    @Autowired
    TranslateUtil translateUtil;

    @Test
    @DisplayName("Get Actions translate by id")
    void getActionsTranslate() {
        Set<Action> actions = new HashSet<>();
        Set<ActionTranslate> actionTranslates = getActionTranslates();
        List<Language> languages = getLanguages();
        List<ActionTranslate> answ = new ArrayList<>();
        answ.add(ActionTranslate.builder()
                .language(languages.get(0)).context("az").build());
        actions.add(Action.builder().actionTranslates(actionTranslates).build());
        assertEquals(answ, translateUtil.getActionsTranslate(actions, 1l));
    }

    @Test
    @DisplayName("Get Question translate by id")
    void getQuestionTranslate() {
        Question question = Question.builder().questionTranslates(getQuestionTranslates()).build();
        assertEquals("az\nru", translateUtil.getQuestionTranslate(question, 1l));
    }


    public List<Language> getLanguages() {
        List<Language> languages = new ArrayList<>();
        languages.add(Language.builder().lang("az").id(1l).build());
        languages.add(Language.builder().lang("ru").id(2l).build());
        languages.add(Language.builder().lang("en").id(3l).build());
        return languages;
    }

    public List<QuestionTranslate> getQuestionTranslates() {
        List<QuestionTranslate> questionTranslates = new ArrayList<>();
        List<Language> languages = getLanguages();
        questionTranslates.add(QuestionTranslate.builder()
                .context("az")
                .language(languages.get(0)).build());
        questionTranslates.add(QuestionTranslate.builder()
                .context("ru")
                .language(languages.get(0)).build());
        return questionTranslates;
    }

    public Set<ActionTranslate> getActionTranslates() {
        Set<ActionTranslate> actionTranslates = new HashSet<>();
        List<Language> languages = getLanguages();
        actionTranslates.add(ActionTranslate.builder()
                .language(languages.get(0)).context("az").build());
        actionTranslates.add(ActionTranslate.builder()
                .language(languages.get(1)).context("ru").build());
        actionTranslates.add(ActionTranslate.builder()
                .language(languages.get(2)).context("en").build());
        return actionTranslates;
    }
}
