package az.code.telegram_bot.exceptions;

public class StopBeforeException extends Exception implements MyCustomException {

    String ru = "Прежде чем начать новый запрос нужно остановить старый." +
            " Воспользуйтесь командой /stop для остановки старого запроса!";
    String en = "Before starting a new request, you need to stop the old one." +
            " Use the /stop command to stop the old request!";
    String az = "Yeni bir sorğuya başlamazdan əvvəl köhnəni dayandırmalısınız." +
            " Köhnə sorğunu dayandırmaq üçün /stop əmrindən istifadə edin!";

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