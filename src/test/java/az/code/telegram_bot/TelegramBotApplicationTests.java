package az.code.telegram_bot;

import az.code.telegram_bot.botApi.TelegramFacade;
import az.code.telegram_bot.botApi.handlers.InputMessageHandler;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.models.*;
import az.code.telegram_bot.models.enums.ActionType;
import az.code.telegram_bot.utils.CalendarUtil;
import az.code.telegram_bot.utils.TranslateUtil;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class TelegramBotApplicationTests {
    @Autowired
    TelegramFacade facade;
    @Autowired
    CalendarUtil calendarUtil;

    @Test
    void isCommand() {
        Message message = new Message();
        message.setText("/start");
        Assert.assertEquals(true, facade.isCommand(message));
    }

    @Test
    void isNotCommand() {
        Message message = new Message();
        message.setText("start");
        Assert.assertEquals(false, facade.isCommand(message));
    }


    @Test
    @DisplayName("Get calendar control buttons for last month")
    void getControlButtonsLastMoth() {
        List<InlineKeyboardButton> controlsRow = new ArrayList<>();
        LocalDate date = LocalDate.parse(LocalDate.now().plusYears(1).getYear() + "-12-05");
        controlsRow.add(InlineKeyboardButton.builder()
                .callbackData(date.getYear() + "-11")
                .text("<").build());
        controlsRow.add(InlineKeyboardButton.builder()
                .callbackData(date.plusYears(1).getYear() + "-1")
                .text(">").build());
        assertEquals(controlsRow, calendarUtil.getControlButtons(date));

    }

    @Test
    @DisplayName("Get calendar control for this month")
    void getControlButtonsForThisMoth() {
        List<InlineKeyboardButton> controlsRow = new ArrayList<>();
        LocalDate date = LocalDate.now();
        controlsRow.add(InlineKeyboardButton.builder()
                .callbackData(date.getYear() + "-" + date.plusMonths(1).getMonthOfYear())
                .text(">").build());
        assertEquals(controlsRow, calendarUtil.getControlButtons(date));
    }

    @Test
    @DisplayName("Get month by date ")
    void getMonth() {
        calendarUtil.langId = 1;
        LocalDate date = LocalDate.parse("2021-12-05");
        assertEquals(
                Collections.singletonList(InlineKeyboardButton.builder()
                        .callbackData(CalendarUtil.IGNORE).text("дек. 2021").build())
                , calendarUtil.getMonth(date));
    }

    @Test
    @DisplayName("Get weekdays buttons")
    void getWDButtons() {
        calendarUtil.langId = 2;
        List<InlineKeyboardButton> daysOfWeekRow = new ArrayList<>();
        for (String day : CalendarUtil.WD_AZ) {
            daysOfWeekRow.add(InlineKeyboardButton.builder()
                    .callbackData(CalendarUtil.IGNORE).text(day).build());
        }
        assertEquals(daysOfWeekRow, calendarUtil.getWDButtons());
    }

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

    @Autowired
    InputMessageHandler inputMessageHandler;

    @Test
    void isCalendarQuestionTrue() {
        Question currentQuestion = Question.builder()
                .actions(Collections.singleton(Action.builder().type(ActionType.CALENDAR).build()))
                .build();
        assertEquals(true, inputMessageHandler.isCalendarQuestion(currentQuestion));
    }

    @Test
    void isCalendarQuestionFalse() {
        Question currentQuestion = Question.builder()
                .actions(Collections.singleton(Action.builder().type(ActionType.FREETEXT).build()))
                .build();
        assertEquals(false, inputMessageHandler.isCalendarQuestion(currentQuestion));
    }
}
