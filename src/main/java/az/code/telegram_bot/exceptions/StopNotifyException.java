package az.code.telegram_bot.exceptions;

public class StopNotifyException extends Exception implements MyCustomException {

    String ru = "Вы остановили запрос!";
    String en = "You have stopped the request!";
    String az = "Sorğunu dayandırdınız!";

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