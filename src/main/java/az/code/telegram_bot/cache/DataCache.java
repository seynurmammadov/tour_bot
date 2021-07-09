package az.code.telegram_bot.cache;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.UserData;

public interface DataCache {
    void setQuestion(long userId, Question question);

    Question getCurrentQuestion(long userId);
    boolean setFirstQuestion(long userId);

    UserData getUserProfileData(long userId);

    void saveUserProfileData(long userId, UserData userData);

    void setLanguage(long userId,String langName);
    void addAnswer(long userId,String answer);
    void clearData(Long userId);
}
