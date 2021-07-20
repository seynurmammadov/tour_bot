package az.code.telegram_bot.exceptions;

public class StopNotifyException extends Exception implements MyCustomException {

    String ru = "Вы остановили запрос!";
    String en = "You have stopped the request!";
    String az = "Sorğunu dayandırdınız!";

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