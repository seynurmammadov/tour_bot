package az.code.telegram_bot.exceptions;

public class RequestWasStoppedException extends Exception implements MyCustomException {

    String ru = "Из-за того что у агентсв не имеется предложений по вашему запросу, мы решили остановить ваш запрос. " +
            "\n Если вы хотите начать новый запрос воспользуйтесь командой /start.";
    String en = "Due to the fact that the agencies do not have offers for your request, we decided to stop your request." +
            " If you want to start a new request, use the /start command.";
    String az = "Agentliklərin sorğunuz üçün heç bir təklifi olmadığına görə sorğunuzu dayandırmağı qərara verdik." +
            " Yeni bir sorğu başlatmaq istəyirsinizsə, /start əmrini istifadə edə bilərsiz.";

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