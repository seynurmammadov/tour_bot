package az.code.telegram_bot.exceptions;

public interface MyCustomException {
    String getAz();
    String getRu();
    String getEn();
    default String getLocalizedMessage(Long langId) {
        switch (langId.intValue()) {
            case 1:
                return getRu();
            case 2:
                return getAz();
            default:
                return getEn();
        }
    };
}
