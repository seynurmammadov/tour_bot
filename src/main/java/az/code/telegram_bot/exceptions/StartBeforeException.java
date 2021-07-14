package az.code.telegram_bot.exceptions;

public class StartBeforeException extends Exception implements MyCustomException {

    String ru = "Пожалуйста, воспользуйтесь командой /start из меню для того чтобы начать!";
    String en = "Please use the /start command from the menu to get started!";
    String az = "Zəhmət olmasa başlamaq üçün menyudan /start əmrini istifadə edin!";

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
