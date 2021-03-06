package az.code.telegram_bot.cache;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.UserData;

import java.io.IOException;

public interface DataCache {
    void setQuestion(long userId, Question question);

    Question getCurrentQuestion(long userId);

    void setFirstQuestion(long userId);

    void setUUID(long userId, String UUID);

    UserData getUserData(long userId);

    void saveUserProfileData(long userId, UserData userData);

    void setLanguage(long userId, String langName);

    void addAnswer(long userId, String answer);

    void clearDataAndState(Long userId) throws IOException;

    void clearData(Long userId);
}
