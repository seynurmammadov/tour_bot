package az.code.telegram_bot.exceptions;

public class IncorrectAnswerException extends Exception implements MyCustomException{

    //TODO get text with properties
    String ru = "Пожалуйста, выберите один из правильных ответов!";
    String en = "Please choose one of the correct answers!";
    String az = "Zəhmət olmasa verilən düzgün cavablardan seçin!";
    @Override
    public String getLocalizedMessage(Long langId) {

        switch (langId.intValue()){
            case 1: return ru;
            case 2: return az;
            case 3: return en;
            default: return en;
        }
    }
}