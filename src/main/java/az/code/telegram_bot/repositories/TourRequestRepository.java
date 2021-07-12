package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.TourRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TourRequestRepository extends JpaRepository<TourRequest, Long> {
    @Query("select t from TourRequest t where t.UUID=:UUID and t.status=true")
    Optional<TourRequest> getByUUID(String UUID);
    @Modifying
    @Transactional
    @Query("update TourRequest t set t.status=false where t.client_id=:userId")
    void deactiveSeance(Long userId);
    @Query("select t from TourRequest t where t.client_id=:clientID and t.status=true")
    Optional<TourRequest> getByClient_id(Long clientID);
}
