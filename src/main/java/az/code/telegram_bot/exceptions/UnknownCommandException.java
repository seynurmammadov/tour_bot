package az.code.telegram_bot.exceptions;

public class UnknownCommandException extends Exception implements MyCustomException {

    //TODO get text with properties
    String ru = "Неопознанная команда!";
    String en = "Unknown command!";
    String az = "Naməlum əmr!";

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
