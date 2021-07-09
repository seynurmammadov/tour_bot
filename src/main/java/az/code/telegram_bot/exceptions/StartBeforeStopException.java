package az.code.telegram_bot.exceptions;

public class StartBeforeStopException extends Exception implements MyCustomException {

    //TODO get text with properties
    String ru = "У вас нет активного запроса чтобы остановить.Для того чтобы создать запрос воспользуйтесь командой /start из меню.";
    String en = "You do not have an active request to stop. To create a request use the /start command from the menu.";
    String az = "Dayandırmaq üçün aktiv bir sorğunuz yoxdur. Sorğu yaratmaq üçün menyudan /start əmrini istifadə edin.";

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
