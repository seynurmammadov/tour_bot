package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.cache.DataCache;
import az.code.telegram_bot.configs.RabbitMQConfig;
import az.code.telegram_bot.exceptions.IncorrectAnswerException;
import az.code.telegram_bot.exceptions.OfferShouldBeRepliedException;
import az.code.telegram_bot.exceptions.StartBeforeException;
import az.code.telegram_bot.models.AcceptedOffer;
import az.code.telegram_bot.models.Action;
import az.code.telegram_bot.models.Question;
import az.code.telegram_bot.models.enums.ActionType;
import az.code.telegram_bot.models.enums.StaticStates;
import az.code.telegram_bot.repositories.RedisRepository;
import az.code.telegram_bot.services.Interfaces.BotSessionService;
import az.code.telegram_bot.services.Interfaces.MessageService;
import az.code.telegram_bot.services.Interfaces.QuestionService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;


@Component
public class InputMessageHandler implements MessageHandler {
    final
    MessageService messageService;
    final
    QuestionService questionService;
    final
    DataCache dataCache;
    final
    RabbitTemplate template;
    final
    BotSessionService sessionService;
    final
    RedisRepository<AcceptedOffer> acceptedOfferRepository;

    private Long userId;
    private String chatId;
    private TelegramWebHook bot;

    public InputMessageHandler(MessageService messageService, QuestionService questionService,
                               DataCache dataCache, RabbitTemplate template, RedisRepository<AcceptedOffer> acceptedOfferRepository, BotSessionService sessionService) {
        this.messageService = messageService;
        this.questionService = questionService;
        this.dataCache = dataCache;
        this.template = template;
        this.acceptedOfferRepository = acceptedOfferRepository;
        this.sessionService = sessionService;
    }

    /**
     * Input message handler method
     *
     * @param message user response message
     * @param bot     TelegramWebHook for sending additional messages
     * @return question with actionType, if was last question or if user answer is incorrect {@code null}
     */
    @Override
    public SendMessage handle(Message message, TelegramWebHook bot, boolean command) throws TelegramApiException, IOException {
        setData(message, bot);
        Question currentQuestion = dataCache.getCurrentQuestion(userId);
        SendMessage sendMessage = checkQuestionStatus(currentQuestion);
        if (sendMessage != null) {
            return sendMessage;
        }
        if (!command) {
            checkAnswerNCaching(currentQuestion, message);
            currentQuestion = dataCache.getCurrentQuestion(userId);
        }
        return getMessage(currentQuestion, message.getText());
    }

    private SendMessage checkQuestionStatus(Question currentQuestion) {
        if (currentQuestion == null) {
            return messageService.createError(chatId,
                    new StartBeforeException(),
                    dataCache.getUserData(userId).getLangId());
        } else if (Objects.equals(currentQuestion.getState(), StaticStates.END.toString())) {
            return messageService.createError(chatId,
                    new OfferShouldBeRepliedException(),
                    dataCache.getUserData(userId).getLangId());
        } else {
            if (isCalendarQuestion(currentQuestion)) return messageService.createError(chatId,
                    new IncorrectAnswerException(),
                    dataCache.getUserData(userId).getLangId());
        }
        return null;
    }

    public boolean isCalendarQuestion(Question currentQuestion) {
        ActionType actionType = currentQuestion.getActions().stream()
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getType();
        return actionType == ActionType.CALENDAR;
    }

    private void acceptOffer(String phoneNumber) throws IOException {
        template.convertAndSend(RabbitMQConfig.exchange,
                RabbitMQConfig.cancelled,
                dataCache.getUserData(userId).getUUID());
        AcceptedOffer offer = acceptedOfferRepository.findById(userId);
        offer.setPhoneNumber(phoneNumber);
        template.convertAndSend(RabbitMQConfig.exchange, RabbitMQConfig.accepted, offer);
        dataCache.clearDataAndState(userId);
        sessionService.deactivate(userId);
    }

    /**
     * <p>Method checks if the user's answer is correct and caches the answer if the answer is.</p>
     * <p>If the answer is not correct, an appropriate error message is sent.</p>
     *
     * @param question the question answered by the user
     * @param message  user response message
     */
    private void checkAnswerNCaching(Question question, Message message) throws TelegramApiException {
        Action action = question.getActions().iterator().next();
        if (action.getType() == ActionType.FREETEXT) {
            regexFreeText(question, message, action);
        } else if (action.getType() == ActionType.BUTTON) {
            regexButton(question, message);
        } else if (action.getType() == ActionType.BUTTON_CONTACT_INFO) {
            cachingDataNChangeState(question, message, action);
        } else if (action.getType() == ActionType.CALENDAR_ANSWER) {
            bot.execute(messageService.editCalendarMessage(
                    question,
                    message,
                    dataCache.getUserData(userId).getLangId())
            );
            cachingDataNChangeState(question, message, action);
        }
    }

    /**
     * Method checks the regular expression of the response (ActionType.BUTTON)
     *
     * @param question the question answered by the user
     * @param message  user response message
     */
    private void regexButton(Question question, Message message) throws TelegramApiException {
        Optional<Action> filteredAnswer = question.getActions().stream()
                .filter(a -> a.getActionTranslates().stream()
                        .anyMatch(t -> t.getContext().equals(message.getText())))
                .findFirst();

        if (filteredAnswer.isPresent()) {
            cachingDataNChangeState(question, message, filteredAnswer.get());
        } else if (question.getState() != null) {
            bot.execute(messageService.createError(chatId,
                    new IncorrectAnswerException(),
                    dataCache.getUserData(userId).getLangId()));
        }
    }

    /**
     * Method checks the regular expression of the response (ActionType.FREETEXT)
     *
     * @param question the question answered by the user
     * @param message  user response message
     * @param action   an action that contains the correct answers
     */
    private void regexFreeText(Question question, Message message, Action action) throws TelegramApiException {
        if (!Pattern.matches(question.getRegex(), message.getText())) {
            bot.execute(messageService.createError(chatId, new IncorrectAnswerException(),
                    dataCache.getUserData(userId).getLangId()));
        } else {
            cachingDataNChangeState(question, message, action);
        }
    }

    /**
     * Method caches user response data
     *
     * @param question       the question answered by the user
     * @param message        user response message
     * @param filteredAnswer action found on response
     */
    private void cachingDataNChangeState(Question question, Message message, Action filteredAnswer) {
        if (question.getState() == null) {
            dataCache.setLanguage(userId, message.getText());
            Question state = dataCache.getCurrentQuestion(userId);
            state.setState("language");
            dataCache.setQuestion(userId, state);
        }
        if (filteredAnswer.getKeyword() != null) {
            dataCache.addAnswer(userId, filteredAnswer.getKeyword());
        } else {
            dataCache.addAnswer(userId, message.getText());
        }
        dataCache.setQuestion(userId, filteredAnswer.getNextQuestion());
    }

    /**
     * Method send collected user data to rabbit mq
     */
    //TODO if stop before send dont sent request to stop queue
    private void sendCollectedData() throws TelegramApiException {
        template.convertAndSend(RabbitMQConfig.exchange,
                RabbitMQConfig.sent,
                dataCache.getUserData(userId));
        bot.execute(messageService.createMsgWithData(chatId, userId, dataCache.getUserData(userId).toString()));
        dataCache.setQuestion(userId, Question.builder().state(StaticStates.END.toString()).build());
        dataCache.clearData(userId);
    }

    /**
     * Method creates a message dynamically using data from the database and
     * send collected data if question was last.
     *
     * @param question the question answered by the user
     * @return Message which created by actionType
     */
    public SendMessage getMessage(Question question, String userAnswer) throws TelegramApiException, IOException {
        long langId = dataCache.getUserData(userId).getLangId();
        ActionType actionType = question.getActions().stream()
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getType();
        sendOfferOrAnswers(question, userAnswer);
        return messageService.getMessageByAction(question, langId, actionType, chatId);
    }


    private void sendOfferOrAnswers(Question question, String userAnswer) throws TelegramApiException, IOException {
        boolean end = Objects.equals(question.getState(), StaticStates.REPLY_END.toString());
        Optional<Action> questionAction = question.getActions().stream().findFirst();
        if (questionAction.isPresent()) {
            if (questionAction.get().getNextQuestion() == null && !end) {
                sendCollectedData();
            }
        }
        if (end) {
            acceptOffer(userAnswer);
        }
    }

    /**
     * Method sets data that is used in other methods.
     *
     * @param message user response message
     * @param bot     TelegramWebHook for sending additional messages
     */
    private void setData(Message message, TelegramWebHook bot) {
        this.userId = message.getFrom().getId();
        this.chatId = message.getChatId().toString();
        this.bot = bot;
    }

}
