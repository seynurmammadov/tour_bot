package az.code.telegram_bot.exceptions;

public class StopNotifyException extends Exception implements MyCustomException {

    String ru = "Запрос остановлен!";
    String en = "The request have stopped!";
    String az = "Sorğunuz dayandırıldı!";

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