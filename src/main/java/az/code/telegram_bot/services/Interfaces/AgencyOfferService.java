package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.models.AgencyOffer;

import java.util.List;
import java.util.Optional;

public interface AgencyOfferService {
    void save(AgencyOffer agencyOffer);
    void clearData(String UUID);
    void deleteOffer(AgencyOffer offer);
    List<AgencyOffer> getAllByUUID(String UUID);
    Optional<AgencyOffer> getByMessageIdAndUUID(Integer messageId,String UUID);
}
