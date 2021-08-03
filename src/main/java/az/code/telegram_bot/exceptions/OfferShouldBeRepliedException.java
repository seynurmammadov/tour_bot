package az.code.telegram_bot.exceptions;

public class OfferShouldBeRepliedException extends Exception implements MyCustomException {

    String ru = "Пожалуйста,прежде чем продолжить ответьте на какое-то из отправленный предложений которым вы еще не ответили, если таковы имеются, " +
            " или воспользуйтесь командой /stop чтобы остановить текущий запрос!";
    String en ="Please, before proceeding, answer any of the submitted proposals that you have not yet replied to, if any," +
            " use the /stop command to stop the current request!";
    String az =   "Zəhmət olmasa, davam etməzdən əvvəl, hələ cavablandırmadığınız təqdim olunan təkliflərin birinə cavab verin və" +
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