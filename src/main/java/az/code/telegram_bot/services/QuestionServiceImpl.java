package az.code.telegram_bot.services;

import az.code.telegram_bot.models.Action;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.repositories.QuestionRepository;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    final
    QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question getQuestionByKeyword(String keyword) {
        return questionRepository.getQuestionByKeyword(keyword);
    }

    @Override
    public Question getFirstQuestion() {
        return questionRepository.getFirstQuestion();
    }

    @Override
    public Question getSecondQuestion() {
        Optional<Action> action = getFirstQuestion().getActions().stream().findFirst();
        return action.map(Action::getNextQuestion).orElse(null);
    }
}

