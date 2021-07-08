package az.code.telegram_bot.services;

import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.repositories.QuestionRepository;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    final
    QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question getQuestionByKeyword(Question question) {
        return questionRepository.getQuestionByKeyword(question.getState());
    }
    @Override
    public Question getFirstQuestion() {
        return questionRepository.getFirstQuestion();
    }
}

