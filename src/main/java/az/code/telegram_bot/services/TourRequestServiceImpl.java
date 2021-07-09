package az.code.telegram_bot.services;

import az.code.telegram_bot.models.TourRequest;
import az.code.telegram_bot.repositories.TourRequestRepository;
import az.code.telegram_bot.services.Interfaces.TourRequestService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class TourRequestServiceImpl implements TourRequestService {

    final
    TourRequestRepository tourRepository;

    public TourRequestServiceImpl(TourRequestRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    @Override
    public void createSeance(Long userId, String chatId) {
        tourRepository.save(TourRequest.builder()
                .status(true)
                .createdAt(LocalDateTime.now())
                .client_id(userId)
                .chatId(chatId)
                .UUID(UUID.randomUUID().toString())
                .build());
    }

    @Override
    public Optional<TourRequest> getByUUID(String UUID) {
        return tourRepository.getByUUID(UUID);
    }

}
