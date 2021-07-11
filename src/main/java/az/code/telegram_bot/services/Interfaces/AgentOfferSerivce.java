package az.code.telegram_bot.services.Interfaces;

import az.code.telegram_bot.models.AgentOffer;

public interface AgentOfferSerivce {
    void create(AgentOffer agentOffer);
    void clearData(String UUID);
}
