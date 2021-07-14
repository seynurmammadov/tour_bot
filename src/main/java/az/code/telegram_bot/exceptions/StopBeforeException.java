package az.code.telegram_bot.exceptions;

public class StopBeforeException extends Exception implements MyCustomException {

    String ru = "Прежде чем начать новый запрос нужно остановить старый." +
            " Воспользуйтесь командой /stop для остановки старого запроса!";
    String en = "Before starting a new request, you need to stop the old one." +
            " Use the /stop command to stop the old request!";
    String az = "Yeni bir sorğuya başlamazdan əvvəl köhnəni dayandırmalısınız." +
            " Köhnə sorğunu dayandırmaq üçün /stop əmrindən istifadə edin!";

    @Override
    public String getLocalizedMessage(Long langId) {

        switch (langId.intValue()) {
            case 1:
                return ru;
            case 2:
                return az;
            default:
                return en;
        }
    }
}