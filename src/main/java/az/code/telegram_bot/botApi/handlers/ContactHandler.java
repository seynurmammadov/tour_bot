package az.code.telegram_bot.botApi.handlers;

import az.code.telegram_bot.TelegramWebHook;
import az.code.telegram_bot.botApi.handlers.interfaces.MessageHandler;
import az.code.telegram_bot.models.AcceptedOffer;
import az.code.telegram_bot.repositories.AcceptedOfferRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Component
public class ContactHandler implements MessageHandler {

    AcceptedOfferRepository acceptedOfferRepository;
    final
    MessageHandler inputMessageHandler;

    public ContactHandler(AcceptedOfferRepository acceptedOfferRepository,
                          @Qualifier("inputMessageHandler") MessageHandler inputMessageHandler) {
        this.acceptedOfferRepository = acceptedOfferRepository;
        this.inputMessageHandler = inputMessageHandler;
    }

    @Override
    public SendMessage handle(Message message, TelegramWebHook bot, boolean isCommand) throws TelegramApiException, IOException {
        message.setText(message.getContact().getPhoneNumber());
        AcceptedOffer acceptedOffer = acceptedOfferRepository.findById(message.getFrom().getId());
        acceptedOffer.setTelegramNumber(true);
        acceptedOfferRepository.save(message.getFrom().getId(), acceptedOffer);
        return inputMessageHandler.handle(message, bot, false);
    }
}
