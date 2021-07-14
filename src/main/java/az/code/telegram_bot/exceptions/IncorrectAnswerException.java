package az.code.telegram_bot.exceptions;

public class IncorrectAnswerException extends Exception implements MyCustomException {

    String ru = "Пожалуйста, выберите или впишите правильный ответ!";
    String en = "Please select or enter the correct answer!";
    String az = "Zəhmət olmasa düzgün cavabı seçin və ya daxil edin!";

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