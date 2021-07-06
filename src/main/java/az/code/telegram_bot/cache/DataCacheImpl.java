package az.code.telegram_bot.cache;

import az.code.telegram_bot.models.UserData;
import az.code.telegram_bot.models.enums.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataCacheImpl implements DataCache {

    private Map<Long, BotState> usersBotStates = new HashMap<>();
    private Map<Long, UserData> usersData = new HashMap<>();

    @Override
    public void setBotState(long userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getBotState(long userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.FILLING_DATA;
        }
        return botState;
    }

    @Override
    public UserData getUserProfileData(long userId) {
        UserData userProfileData = usersData.get(userId);
        if (userProfileData == null) {
            userProfileData = new UserData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(long userId, UserData userData) {
        usersData.put(userId, userData);
    }
}
