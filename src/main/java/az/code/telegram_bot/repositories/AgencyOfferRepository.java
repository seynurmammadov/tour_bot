package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.AgencyOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface AgencyOfferRepository extends JpaRepository<AgencyOffer, Long> {
    @Modifying
    @Transactional
    @Query("delete from AgencyOffer o where o.UUID=:UUID")
    void deleteByUUID(String UUID);

    @Query("SELECT o from AgencyOffer o where o.UUID=:UUID and o.filePath is not null ")
    List<AgencyOffer> getAgentOffersByUUID(String UUID);

    @Query("SELECT o from AgencyOffer o where o.messageId=:messageId and  o.UUID=:UUID and o.isAccepted=false")
    Optional<AgencyOffer> getByMessageIdAndUUID(Integer messageId,String UUID);
}
