package az.code.telegram_bot.cache;

import az.code.telegram_bot.models.UserData;
import az.code.telegram_bot.models.enums.BotState;

public interface DataCache {
    void setBotState(long userId, BotState botState);

    BotState getBotState(long userId);

    UserData getUserProfileData(long userId);

    void saveUserProfileData(long userId, UserData userData);
}
