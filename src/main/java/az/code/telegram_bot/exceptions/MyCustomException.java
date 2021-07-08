package az.code.telegram_bot.exceptions;

public interface MyCustomException {
    String getLocalizedMessage(Long langId);
}
