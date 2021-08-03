package az.code.telegram_bot.exceptions;

public class OfferShouldBeRepliedException extends Exception implements MyCustomException {

    String ru = "Пожалуйста, ответьте на какое-то из отправленный предложений которым вы еще не ответили, если таковы имеются, " +
            "прежде чем продолжить или воспользуйтесь командой /stop чтобы остановить текущий запрос!";
    String en = "Please respond to any of the submitted proposals that you have not yet replied to, if any," +
            "before continuing or use the /stop command to stop the current request!";
    String az = "Zəhmət olmasa davam etmədən evvəl göndətilmiş, amma hələ cavablandırmadığınız təkliflərdən hər hansı birinə cavab verin və" +
            " ya mövcud sorğunu dayandırmaq üçün /stop əmrindən istifadə edin!";

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