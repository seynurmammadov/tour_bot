package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.models.TourRequest;

import java.util.Optional;

public interface TourRequestService {
    void createSeance(Long userId, String chatId);
    Optional<TourRequest> getByUUID(String UUID);
}
