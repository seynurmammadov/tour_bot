package az.code.telegram_bot.services;

import az.code.telegram_bot.models.AgencyOffer;
import az.code.telegram_bot.repositories.AgencyOfferRepository;
import az.code.telegram_bot.services.Interfaces.AgencyOfferService;
import az.code.telegram_bot.utils.FileUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class AgencyOfferImpl implements AgencyOfferService {
    final
    AgencyOfferRepository offerRepository;

    final
    FileUtil fileUtil;

    public AgencyOfferImpl(AgencyOfferRepository receiverRepository, FileUtil fileUtil) {
        this.offerRepository = receiverRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void save(AgencyOffer agencyOffer) {
        offerRepository.save(agencyOffer);
    }

    @Override
    public void clearData(String UUID) throws IOException {
        deleteLocal(UUID);
        offerRepository.deleteByUUID(UUID);
    }

    @Override
    public void delete(AgencyOffer offer) {
        offerRepository.delete(offer);
    }

    private void deleteLocal(String UUID) throws IOException {
        List<AgencyOffer> agencyOffers = getAllByUUID(UUID);
        for (AgencyOffer offer: agencyOffers) {
            fileUtil.deleteWithPath(offer.getFilePath());
        }

    }

    public List<AgencyOffer> getAllByUUID(String UUID) {
        return offerRepository.getAgentOffersByUUID(UUID);
    }

    @Override
    public Optional<AgencyOffer> getByMessageIdAndUUID(Integer messageId, String UUID) {
        return offerRepository.getByMessageIdAndUUID(messageId, UUID);
    }
}
