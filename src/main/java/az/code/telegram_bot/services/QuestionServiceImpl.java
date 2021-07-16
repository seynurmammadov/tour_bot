package az.code.telegram_bot.services;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.repositories.QuestionRepository;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class QuestionServiceImpl implements QuestionService {

    final
    QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question getByKeyword(String keyword) {
        return questionRepository.getQuestionByKeyword(keyword);
    }

    @Override
    @Cacheable("firstQuestion")
    public Question getFirst() {
        return questionRepository.getFirstQuestion();
    }

}

