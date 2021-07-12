package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.models.AgentOffer;

import java.util.List;

public interface AgentOfferService {
    void create(AgentOffer agentOffer);
    void clearData(String UUID);
    void deleteOffer(AgentOffer offer);
    List<AgentOffer> getAllByUUID(String UUID);
}
