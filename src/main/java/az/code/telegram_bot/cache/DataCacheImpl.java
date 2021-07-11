package az.code.telegram_bot.cache;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.UserData;
import az.code.telegram_bot.repositories.LanguageRepository;
import az.code.telegram_bot.repositories.RedisRepository;
import az.code.telegram_bot.services.Interfaces.AgentOfferSerivce;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.stereotype.Component;

import java.util.HashMap;

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
    final
    AgentOfferSerivce receiverService;

    public DataCacheImpl(QuestionService questionService, LanguageRepository languageRepository,
                         MessageService messageService, RedisRepository<UserData> userDataRepository,
                         RedisRepository<Question> stateRepository, AgentOfferSerivce receiverService) {
        this.questionService = questionService;
        this.languageRepository = languageRepository;
        this.messageService = messageService;
        this.userDataRepository = userDataRepository;
        this.stateRepository = stateRepository;
        this.receiverService = receiverService;
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
    public boolean setPrimaryQuestion(long userId) {
        if (getCurrentQuestion(userId) == null && getUserProfileData(userId).getLangId() == 4l) {
            setQuestion(userId, questionService.getFirstQuestion());
            return true;
        } else if (getCurrentQuestion(userId) == null) {
            setQuestion(userId, questionService.getSecondQuestion());
            return true;
        }
        return false;
    }

    @Override
    public UserData getUserProfileData(long userId) {
        UserData userProfileData = userDataRepository.findById(userId);
        if (userProfileData == null) {
            return new UserData();
        } else if (userProfileData.getAnswers() == null) {
            userProfileData.setAnswers(new HashMap<>());
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
    public void setUUID(long userId, String UUID) {
        UserData userData = getUserProfileData(userId);
        userData.setUUID(UUID);
        saveUserProfileData(userId, userData);
    }

    @Override
    public void addAnswer(long userId, String answer) {
        UserData userData = getUserProfileData(userId);
        userData.addAnswer(answer, getCurrentQuestion(userId).getState());
        saveUserProfileData(userId, userData);
    }

    @Override
    public void clearDataAndState(Long userId) {
        UserData userData = getUserProfileData(userId);
        receiverService.clearData(userData.getUUID());
        stateRepository.delete(userId);
        userDataRepository.delete(userId);
        saveUserProfileData(userId, UserData.builder().langId(userData.getLangId()).build());
    }

    @Override
    public void clearData(Long userId) {
        UserData userData = getUserProfileData(userId);
        userDataRepository.delete(userId);
        saveUserProfileData(userId, UserData.builder()
                .langId(userData.getLangId())
                .UUID(userData.getUUID()).build());
    }
}
