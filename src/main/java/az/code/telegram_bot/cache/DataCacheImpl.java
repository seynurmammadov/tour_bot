package az.code.telegram_bot.cache;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.UserData;
import az.code.telegram_bot.repositories.LanguageRepository;
import az.code.telegram_bot.services.Interfaces.MessageService;
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
    final
    MessageService messageService;

    private final Map<Long, Question> usersStates = new HashMap<>();
    private final Map<Long, UserData> usersData = new HashMap<>();

    public DataCacheImpl(QuestionService questionService, LanguageRepository languageRepository, MessageService messageService) {
        this.questionService = questionService;
        this.languageRepository = languageRepository;
        this.messageService = messageService;
    }

    @Override
    public void setQuestion(long userId, Question question) {
        usersStates.put(userId, question);
    }

    @Override
    public Question getCurrentQuestion(long userId) {
        Question question = usersStates.get(userId);
        return question;
    }

    @Override
    public boolean setFirstQuestion(long userId) {
        if (getCurrentQuestion(userId) == null) {
            setQuestion(userId, questionService.getFirstQuestion());
            return true;
        }
        return false;
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
    public void addAnswer(long userId, String answer) {
        UserData userData = getUserProfileData(userId);
        userData.addAnswer(answer, getCurrentQuestion(userId).getState());
    }
}
