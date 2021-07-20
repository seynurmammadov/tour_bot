package az.code.telegram_bot.exceptions;

public class UnknownCommandException extends Exception implements MyCustomException {

    String ru = "Неопознанная команда!";
    String en = "Unknown command!";
    String az = "Naməlum əmr!";

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
