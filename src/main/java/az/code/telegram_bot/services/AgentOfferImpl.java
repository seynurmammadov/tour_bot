package az.code.telegram_bot.services;

import az.code.telegram_bot.models.AgentOffer;
import az.code.telegram_bot.repositories.AgentOfferRepository;
import az.code.telegram_bot.services.Interfaces.AgentOfferService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class AgentOfferImpl implements AgentOfferService {
    final
    AgentOfferRepository offerRepository;

    public AgentOfferImpl(AgentOfferRepository receiverRepository) {
        this.offerRepository = receiverRepository;
    }

    @Override
    public void create(AgentOffer agentOffer) {
        offerRepository.save(agentOffer);
    }

    @Override
    public void clearData(String UUID) {
        deleteLocal(UUID);
        offerRepository.deleteByUUID(UUID);
    }

    @Override
    public void deleteOffer(AgentOffer offer) {
        offerRepository.delete(offer);
    }

    private void deleteLocal(String UUID) {
        List<AgentOffer> agentOffers=getAllByUUID(UUID);
        agentOffers.forEach(o->{
            try
            {
                Files.deleteIfExists(Paths.get(o.getFilePath()));
            }
            catch(IOException e)
            {
                System.out.println("Something go wrong!.");
            }
        });
    }
    public List<AgentOffer> getAllByUUID(String UUID){
        return offerRepository.getAgentOffersByUUID(UUID);
    }
}
