package az.code.telegram_bot.cache;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.UserData;
import az.code.telegram_bot.repositories.LanguageRepository;
import az.code.telegram_bot.repositories.RedisRepository;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    final
    RedisRepository<UserData> userDataRepository;
    final
    RedisRepository<Question> stateRepository;

    public DataCacheImpl(QuestionService questionService, LanguageRepository languageRepository,
                         MessageService messageService, RedisRepository<UserData> userDataRepository,
                         RedisRepository<Question> stateRepository) {
        this.questionService = questionService;
        this.languageRepository = languageRepository;
        this.messageService = messageService;
        this.userDataRepository = userDataRepository;
        this.stateRepository = stateRepository;
    }

    @Override
    public void setQuestion(long userId, Question question) {
        stateRepository.save(userId, question);
    }

    @Override
    public Question getCurrentQuestion(long userId) {
        return stateRepository.findById(userId);
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
        UserData userProfileData = userDataRepository.findById(userId);
        if (userProfileData == null) {
            return new UserData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(long userId, UserData userData) {
        userDataRepository.save(userId, userData);
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
        saveUserProfileData(userId,userData);
    }
    @Override
    public void clearDataAndState(Long userId){
        long landId = getUserProfileData(userId).getLangId();
        stateRepository.delete(userId);
        userDataRepository.delete(userId);
        saveUserProfileData(userId,UserData.builder().langId(landId).build());
    }

    @Override
    public void clearData(Long userId) {
        long landId = getUserProfileData(userId).getLangId();
        userDataRepository.delete(userId);
        saveUserProfileData(userId,UserData.builder().langId(landId).build());
    }
}
