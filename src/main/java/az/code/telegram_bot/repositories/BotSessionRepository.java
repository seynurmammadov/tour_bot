package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.BotSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface BotSessionRepository extends JpaRepository<BotSession, Long> {
    @Query("select t from BotSession t where t.UUID=:UUID and t.status=true")
    Optional<BotSession> getByUUID(String UUID);
    @Modifying
    @Transactional
    @Query("update BotSession t set t.status=false where t.client_id=:userId")
    void deactiveSeance(Long userId);
    @Query("select t from BotSession t where t.client_id=:clientID and t.status=true")
    Optional<BotSession> getByClient_id(Long clientID);
}
