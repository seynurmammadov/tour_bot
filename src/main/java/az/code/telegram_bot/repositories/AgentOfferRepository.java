package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.AgentOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface AgentOfferRepository extends JpaRepository<AgentOffer, Long> {
    @Modifying
    @Transactional
    @Query("delete from AgentOffer o where o.UUID=:UUID")
    void deleteByUUID(String UUID);
    @Query("SELECT o from AgentOffer o where o.UUID=:UUID")
    List<AgentOffer> getAgentOffersByUUID(String UUID);
}
