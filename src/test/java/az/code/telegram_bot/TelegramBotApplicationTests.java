package az.code.telegram_bot;

import az.code.telegram_bot.botApi.TelegramFacade;
import az.code.telegram_bot.botApi.handlers.InputMessageHandler;
import az.code.telegram_bot.models.*;
import az.code.telegram_bot.models.enums.ActionType;
import az.code.telegram_bot.services.MessageServiceImpl;
import az.code.telegram_bot.utils.ButtonsUtil;
import az.code.telegram_bot.utils.CalendarUtil;
import az.code.telegram_bot.utils.TranslateUtil;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TelegramBotApplicationTests {
    @Autowired
    TelegramFacade facade;

    @Autowired
    CalendarUtil calendarUtil;

    @Autowired
    InputMessageHandler inputMessageHandler;

    @Autowired
    TranslateUtil translateUtil;

    @Autowired
    ButtonsUtil buttonsUtil;

    @Autowired
    MessageServiceImpl messageService;

    @Test
    void isCommand() {
        Message message = new Message();
        message.setText("/start");
        assertTrue(facade.isCommand(message));
    }

    @Test
    void isNotCommand() {
        Message message = new Message();
        message.setText("start");
        assertFalse(facade.isCommand(message));
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

//    @Test
//    @DisplayName("Get month by date ")
//    void getMonth() {
//        calendarUtil.langId = 1;
//        LocalDate date = LocalDate.parse("2021-12-05");
//        assertEquals(
//                Collections.singletonList(InlineKeyboardButton.builder()
//                        .callbackData(CalendarUtil.IGNORE).text("дек. 2021").build())
//                , calendarUtil.getMonth(date));
//    }

//    @Test
//    @DisplayName("Get weekdays buttons")
//    void getWDButtons() {
//        calendarUtil.langId = 2;
//        List<InlineKeyboardButton> daysOfWeekRow = new ArrayList<>();
//        for (String day : calendarUtil.getWeekdays(new Locale("az"))) {
//            daysOfWeekRow.add(InlineKeyboardButton.builder()
//                    .callbackData(CalendarUtil.IGNORE).text(day).build());
//        }
//        assertEquals(daysOfWeekRow, calendarUtil.getWDButtons());
//    }


    @Test
    @DisplayName("Get Actions translate by id")
    void getActionsTranslate() {
        Set<Action> actions = new HashSet<>();
        Set<ActionTranslate> actionTranslates = getActionTranslates();
        List<Language> languages = getLanguages();
        List<ActionTranslate> answer = new ArrayList<>();
        answer.add(ActionTranslate.builder()
                .language(languages.get(0)).context("az").action(Action.builder().callback("az").build()).build());
        actions.add(Action.builder().actionTranslates(actionTranslates).build());
        assertEquals(answer, translateUtil.getActionsTranslate(actions, 1L));
    }

    @Test
    @DisplayName("Get Question translate by id")
    void getQuestionTranslate() {
        Question question = Question.builder().questionTranslates(getQuestionTranslates()).build();
        assertEquals("az\nru", translateUtil.getQuestionTranslate(question, 1L));
    }


    @Test
    void isCalendarQuestionTrue() {
        Question currentQuestion = Question.builder()
                .actions(Collections.singleton(Action.builder().type(ActionType.CALENDAR).build()))
                .build();
        assertTrue(inputMessageHandler.isCalendarQuestion(currentQuestion));
    }

    @Test
    void isCalendarQuestionFalse() {
        Question currentQuestion = Question.builder()
                .actions(Collections.singleton(Action.builder().type(ActionType.FREETEXT).build()))
                .build();
        assertFalse(inputMessageHandler.isCalendarQuestion(currentQuestion));
    }

    @Test
    void createRepKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (ActionTranslate text : getActionTranslates()) {
            KeyboardRow row = new KeyboardRow();
            row.add(KeyboardButton.builder().text(text.getContext()).requestContact(false).build());
            keyboard.add(row);
        }
        assertEquals(keyboard, buttonsUtil.createRepKeyboard(new ArrayList<>(getActionTranslates())));
    }

    @Test
    void createInlKeyboard() {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (ActionTranslate text : getActionTranslates()) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(
                    InlineKeyboardButton.builder()
                            .callbackData(text.getAction().getCallback())
                            .text(text.getContext())
                            .build()
            );
            rowsInline.add(rowInline);
        }
        assertEquals(rowsInline, buttonsUtil.createInlKeyboard(new ArrayList<>(getActionTranslates())));
    }

    @Test
    void getNextBtnText() {
        Question currentQuestion = Question.builder()
                .actions(Collections.singleton(Action.builder().type(ActionType.FREETEXT).build()))
                .questionTranslates(getQuestionTranslates())
                .build();
        String str = String.format(
                translateUtil.getQuestionTranslate(currentQuestion, 2L),
                3 - 2
        );
        assertEquals(str, messageService.getNextBtnText(BotSession.builder().countOfOffers(3)
                .countOfSent(2).build(), currentQuestion, 2L));
    }
    public List<Language> getLanguages() {
        List<Language> languages = new ArrayList<>();
        languages.add(Language.builder().lang("az").id(1L).build());
        languages.add(Language.builder().lang("ru").id(2L).build());
        languages.add(Language.builder().lang("en").id(3L).build());
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
        actionTranslates.add(ActionTranslate.builder().action(Action.builder().callback("az").build())
                .language(languages.get(0)).context("az").build());
        actionTranslates.add(ActionTranslate.builder().action(Action.builder().callback("ru").build())
                .language(languages.get(1)).context("ru").build());
        actionTranslates.add(ActionTranslate.builder().action(Action.builder().callback("en").build())
                .language(languages.get(2)).context("en").build());
        return actionTranslates;
    }
}
