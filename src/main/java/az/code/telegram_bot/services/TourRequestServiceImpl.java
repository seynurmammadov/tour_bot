package az.code.telegram_bot.services;

import az.code.telegram_bot.models.TourRequest;
import az.code.telegram_bot.repositories.TourRequestRepository;
import az.code.telegram_bot.services.Interfaces.TourRequestService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class TourRequestServiceImpl implements TourRequestService {

    final
    TourRequestRepository tourRepository;

    public TourRequestServiceImpl(TourRequestRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    @Override
    public void createSeance(Long userId, String chatId, String randUUID) {
        tourRepository.save(TourRequest.builder()
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
    public void saveSeance(TourRequest tourRequest) {
        tourRepository.save(tourRequest);
    }

    @Override
    public void deactivateSeance(Long userId) {
        tourRepository.deactiveSeance(userId);
    }

    @Override
    public Optional<TourRequest> getByUUID(String UUID) {
        return tourRepository.getByUUID(UUID);
    }

    @Override
    public Optional<TourRequest> getByUserId(Long userId) {
        return tourRepository.getByClient_id(userId);
    }

}
