package az.code.telegram_bot.models.enums;

import lombok.Getter;

public enum BotState {
    START(false),
    FILLING_DATA(false),
    LANGUAGE(true);

    @Getter
    private final boolean val;

    BotState(boolean val) {
        this.val = val;
    }
}
