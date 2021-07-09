package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.models.TourRequest;

import java.util.Optional;
import java.util.UUID;

public interface TourRequestService {
    void createSeance(Long userId, String chatId);
    void deactiveSeance(Long userId);
    Optional<TourRequest> getByUUID(String UUID);
}
