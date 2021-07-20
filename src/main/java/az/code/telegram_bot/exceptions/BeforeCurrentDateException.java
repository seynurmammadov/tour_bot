package az.code.telegram_bot.exceptions;

public class BeforeCurrentDateException extends Exception implements MyCustomException {

    String ru = "Пожалуйста, выберите дату не позднее сегоднящнего!";
    String en = "Please select a date no later than today!";
    String az = "Zəhmət olmasa bu gündən gec olmayaraq bir tarix seçin!";

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