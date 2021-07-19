package az.code.telegram_bot.cache;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.UserData;
import az.code.telegram_bot.repositories.LanguageRepository;
import az.code.telegram_bot.repositories.RedisRepository;
import az.code.telegram_bot.services.Interfaces.AgencyOfferService;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    AgencyOfferService offerService;

    public DataCacheImpl(QuestionService questionService, LanguageRepository languageRepository,
                         MessageService messageService, RedisRepository<UserData> userDataRepository,
                         RedisRepository<Question> stateRepository, AgencyOfferService offerService) {
        this.questionService = questionService;
        this.languageRepository = languageRepository;
        this.messageService = messageService;
        this.userDataRepository = userDataRepository;
        this.stateRepository = stateRepository;
        this.offerService = offerService;
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
    public void setFirstQuestion(long userId) {
        if (getCurrentQuestion(userId) == null) {
            setQuestion(userId, questionService.getFirst());
        }
    }
    @Override
    public UserData getUserData(long userId) {
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
        UserData userData = getUserData(userId);
        Long langId = languageRepository.getByLangName(langName).getId();
        userData.setLangId(langId);
        saveUserProfileData(userId, userData);
    }

    @Override
    public void setUUID(long userId, String UUID) {
        UserData userData = getUserData(userId);
        userData.setUUID(UUID);
        saveUserProfileData(userId, userData);
    }

    @Override
    public void addAnswer(long userId, String answer) {
        UserData userData = getUserData(userId);
        userData.addAnswer(answer, getCurrentQuestion(userId).getState());
        saveUserProfileData(userId, userData);
    }

    @Override
    public void clearDataAndState(Long userId) throws IOException {
        UserData userData = getUserData(userId);
        offerService.clearData(userData.getUUID());
        stateRepository.delete(userId);
        userDataRepository.delete(userId);

    }

    @Override
    public void clearData(Long userId) {
        UserData userData = getUserData(userId);
        userDataRepository.delete(userId);
        saveUserProfileData(userId, UserData.builder()
                .langId(userData.getLangId())
                .UUID(userData.getUUID()).build());
    }
}
