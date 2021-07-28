package az.code.telegram_bot.exceptions;

public class NoOffersElseException extends Exception implements MyCustomException {

    String ru = "Предложения агентсв на этом закончились. Пожалуйста ответьте на одно из выше отправленных" +
            " предложений чтобы его принять. \nЕсли вы не ответитев течении определенного времени, ваш запрос будет " +
            "остановлен!";
    String en = "The agency's offers ended there. Please answer one of the above offers to accept it.  \n" +
            "If you do not respond within the allotted time, your request will be stopped!";
    String az = "Bundan sonra agentliklərdən təklif gəlməyəcək. Xahiş edirik qəbul etmək üçün yuxarıda təqdim olunmuş " +
            "təkliflərdən birinə cavab verin. \n Ayrılmış vaxt ərzində cavab verməsəniz, sorğunuz dayandırılacaqdır!";

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