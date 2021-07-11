package az.code.telegram_bot.services;

import az.code.telegram_bot.models.AgentOffer;
import az.code.telegram_bot.repositories.AgentOfferRepository;
import az.code.telegram_bot.services.Interfaces.AgentOfferSerivce;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class AgentOfferImpl implements AgentOfferSerivce {
    final
    AgentOfferRepository receiverRepository;

    public AgentOfferImpl(AgentOfferRepository receiverRepository) {
        this.receiverRepository = receiverRepository;
    }

    @Override
    public void create(AgentOffer agentOffer) {
        receiverRepository.save(agentOffer);
    }

    @Override
    public void clearData(String UUID) {
        deleteLocal(UUID);
        receiverRepository.deleteByUUID(UUID);
    }

    private void deleteLocal(String UUID) {
        List<AgentOffer> agentOffers=receiverRepository.getAgentOffersByUUID(UUID);
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
}
