package az.code.telegram_bot.services;

import az.code.telegram_bot.models.BotSession;
import az.code.telegram_bot.repositories.BotSessionRepository;
import az.code.telegram_bot.services.Interfaces.BotSessionService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class BotSessionServiceImpl implements BotSessionService {

    final
    BotSessionRepository sessionRepository;

    public BotSessionServiceImpl(BotSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void createSeance(Long userId, String chatId, String randUUID) {
        sessionRepository.save(BotSession.builder()
                .status(true)
                .countOfOffers(0)
                .countOfSent(0)
                .createdAt(LocalDateTime.now())
                .client_id(userId)
                .chatId(chatId)
                .lock(false)
                .UUID(randUUID)
                .build());
    }

    @Override
    public void saveSeance(BotSession botSession) {
        sessionRepository.save(botSession);
    }

    @Override
    public void deactivateSeance(Long userId) {
        sessionRepository.deactivateSeance(userId);
    }

    @Override
    public Optional<BotSession> getByUUID(String UUID) {
        return sessionRepository.getByUUID(UUID);
    }

    @Override
    public Optional<BotSession> getByUserId(Long userId) {
        return sessionRepository.getByClient_id(userId);
    }

}
