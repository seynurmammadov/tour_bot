package az.code.telegram_bot.exceptions;

public class OfferShouldBeRepliedException extends Exception implements MyCustomException {

    String ru = "Вы можете ответить только на предложения от агентств, которым вы еще не ответили,если таковы имеются!";
    String en ="You can only respond to offers from agencies you have not yet replied to, if any!";
    String az = "Yalnız cavab vermədiyiniz agentliklərin təkliflərinə cavab verə bilərsiniz, əgər beləsi varsa!";

    @Override
    public String getAz() {
        return az;
    }

    @Override
    public String getRu() {
        return ru;
    }

    @Override
    public String getEn() {
        return en;
    }
}