package az.code.telegram_bot.exceptions;

public class OfferShouldBeRepliedException extends Exception implements MyCustomException {

    String ru = "Пожалуйста, ответьте на какое то из отправленный предложений, если таковы имеются, " +
            "прежде чем продолжить или воспользуйтесь командой /stop чтобы остановить текущий запрос!";
    String en = "Please answer any of the submitted proposals, if any, before proceeding, or use the /stop" +
            " command to stop the current request!";
    String az = "Zəhmət olmasa davam etmədən əvvəl təqdim olunan təkliflərdən hər hansı birinə cavab verin və" +
            " ya mövcud sorğunu dayandırmaq üçün /stop əmrindən istifadə edin!";

    @Override
    public String getLocalizedMessage(Long langId) {
        switch (langId.intValue()) {
            case 1:
                return ru;
            case 2:
                return az;
            case 3:
                return en;
            default:
                return en;
        }
    }
}