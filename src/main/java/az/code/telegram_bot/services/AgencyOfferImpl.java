package az.code.telegram_bot.services;

import az.code.telegram_bot.models.AgencyOffer;
import az.code.telegram_bot.repositories.AgencyOfferRepository;
import az.code.telegram_bot.services.Interfaces.AgencyOfferService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Component
public class AgencyOfferImpl implements AgencyOfferService {
    final
    AgencyOfferRepository offerRepository;

    public AgencyOfferImpl(AgencyOfferRepository receiverRepository) {
        this.offerRepository = receiverRepository;
    }

    @Override
    public void save(AgencyOffer agencyOffer) {
        offerRepository.save(agencyOffer);
    }

    @Override
    public void clearData(String UUID) {
        deleteLocal(UUID);
        offerRepository.deleteByUUID(UUID);
    }

    @Override
    public void deleteOffer(AgencyOffer offer) {
        offerRepository.delete(offer);
    }

    private void deleteLocal(String UUID) {
        List<AgencyOffer> agencyOffers = getAllByUUID(UUID);
        agencyOffers.forEach(o -> {
            try {
                Files.deleteIfExists(Paths.get(o.getFilePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<AgencyOffer> getAllByUUID(String UUID) {
        return offerRepository.getAgentOffersByUUID(UUID);
    }

    @Override
    public Optional<AgencyOffer> getByMessageIdAndUUID(Integer messageId, String UUID) {
        return offerRepository.getByMessageIdAndUUID(messageId, UUID);
    }
}
