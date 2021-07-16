package az.code.telegram_bot.utils;


import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class CalendarUtil {

    public static final String IGNORE = "ignore!@#$%^&";

    public static final String[] WD_EN = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    public static final String[] WD_RU = {"ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};
    public static final String[] WD_AZ = {"B.e.", "Ç.a.", "Ç.", "C.a.", "C.", "Ş.", "B."};

    private long langId;

    public InlineKeyboardMarkup generateCalendar(LocalDate date, long langId) {
        this.langId = langId;
        if (date == null) {
            return null;
        }
        return new InlineKeyboardMarkup(getFilledKeyboard(date));
    }

    private List<List<InlineKeyboardButton>> getFilledKeyboard(LocalDate date) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(getMonth(date));
        keyboard.add(getWDButtons());
        keyboard.addAll(addDays(date));
        keyboard.add(getControlButtons(date));
        return keyboard;
    }

    private List<InlineKeyboardButton> getControlButtons(LocalDate date) {
        List<InlineKeyboardButton> controlsRow = new ArrayList<>();
        LocalDate minusM = date.minusMonths(1);
        LocalDate plusM = date.plusMonths(1);
        String nextMonth = plusM.getYear() + "-" + plusM.getMonthOfYear();
        if (!(minusM.isBefore(LocalDate.now()) && !minusM.isEqual(LocalDate.now()))) {
            String prevMonth = minusM.getYear() + "-" + minusM.getMonthOfYear();
            controlsRow.add(createButton(prevMonth, "<"));
        }
        controlsRow.add(createButton(nextMonth, ">"));
        return controlsRow;
    }

    private List<List<InlineKeyboardButton>> addDays(LocalDate date) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        LocalDate firstDay = date.dayOfMonth().withMinimumValue();

        int shift = firstDay.dayOfWeek().get() - 1;
        int daysInMonth = firstDay.dayOfMonth().getMaximumValue();
        int rows = ((daysInMonth + shift) % 7 > 0 ? 1 : 0) + (daysInMonth + shift) / 7;
        for (int i = 0; i < rows; i++) {
            keyboard.add(buildRow(firstDay, shift));
            firstDay = firstDay.plusDays(7 - shift);
            shift = 0;
        }
        return keyboard;
    }

    private List<InlineKeyboardButton> getWDButtons() {
        List<String> WD = new ArrayList<>();
        switch ((int) this.langId) {
            case 1:
                WD.addAll(Arrays.asList(WD_RU));
                break;
            case 2:
                WD.addAll(Arrays.asList(WD_AZ));
                break;
            default:
                WD.addAll(Arrays.asList(WD_EN));
        }
        return getWDButtons(WD);
    }

    private List<InlineKeyboardButton> getWDButtons(List<String> WD) {
        List<InlineKeyboardButton> daysOfWeekRow = new ArrayList<>();
        for (String day : WD) {
            daysOfWeekRow.add(createButton(IGNORE, day));
        }
        return daysOfWeekRow;
    }

    private List<InlineKeyboardButton> getMonth(LocalDate date) {
        List<InlineKeyboardButton> headerRow = new ArrayList<>();
        switch ((int) this.langId) {
            case 1:
                headerRow.add(createButton(IGNORE, date.toString("MMM yyyy", new Locale("ru"))));
                break;
            case 2:
                headerRow.add(createButton(IGNORE, date.toString("MMM yyyy", new Locale("az"))));
                break;
            default:
                headerRow.add(createButton(IGNORE, date.toString("MMM yyyy", new Locale("en"))));
        }
        return headerRow;
    }

    private InlineKeyboardButton createButton(String callBack, String text) {
        return InlineKeyboardButton.builder()
                .callbackData(callBack)
                .text(text).build();
    }

    private List<InlineKeyboardButton> buildRow(LocalDate date, int shift) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        int day = date.getDayOfMonth();
        LocalDate callbackDate = date;
        for (int j = 0; j < shift; j++) {
            row.add(createButton(IGNORE, " "));
        }
        for (int j = shift; j < 7; j++) {
            if (day <= (date.dayOfMonth().getMaximumValue())) {
                row.add(createButton(callbackDate.toString(), Integer.toString(day++)));
                callbackDate = callbackDate.plusDays(1);
            } else {
                row.add(createButton(IGNORE, " "));
            }
        }
        return row;
    }
}
