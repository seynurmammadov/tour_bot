package az.code.telegram_bot.cache;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.UserData;
import az.code.telegram_bot.repositories.LanguageRepository;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataCacheImpl implements DataCache {

    final
    LanguageRepository languageRepository;

    final
    QuestionService questionService;

    private final Map<Long, Question> usersStates = new HashMap<>();
    private final Map<Long, UserData> usersData = new HashMap<>();

    public DataCacheImpl(QuestionService questionService, LanguageRepository languageRepository) {
        this.questionService = questionService;
        this.languageRepository = languageRepository;
    }

    @Override
    public void setState(long userId, Question state) {
        usersStates.put(userId, state);
    }

    @Override
    public Question getState(long userId) {
        Question state = usersStates.get(userId);
        if (state == null) {
            state = questionService.getFirstQuestion();
        }
        return state;
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

    @Override
    public void setLanguage(long userId, String langName) {
        UserData userData = getUserProfileData(userId);
        Long langId = languageRepository.getByLangName(langName).getId();
        userData.setLangId(langId);
        saveUserProfileData(userId, userData);
    }

    @Override
    public void addAnswer(long userId, String message) {

    }
}
