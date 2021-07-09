package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.TourRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TourRequestRepository extends JpaRepository<TourRequest, Long> {
    @Query("select t from TourRequest t where t.UUID=:UUID and t.status=true")
    Optional<TourRequest> getByUUID(String UUID);
}
