package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.models.BotSession;

import java.util.Optional;

public interface BotSessionService {
    void createSeance(Long userId, String chatId,String UUID);
    void saveSeance(BotSession botSession);
    void deactivateSeance(Long userId);
    Optional<BotSession> getByUUID(String UUID);
    Optional<BotSession>getByUserId(Long userId);
}
