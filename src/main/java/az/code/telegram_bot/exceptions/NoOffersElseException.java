package az.code.telegram_bot.exceptions;

public class NoOffersElseException extends Exception implements MyCustomException {

    String ru = "Предложения агентсв на этом закончились. Пожалуйста ответьте на одно из выше отправленных" +
            " предложений чтобы его принять, eсли этого не сделали." +
            "Ваш запрос будет остановлен через некоторое время!";
    String en = "The agency's offers ended there. Please answer one of the above offers to accept it,\n" +
            "if not done.  \n" +
            "Your request will be stopped after a while!";
    String az = "Bundan sonra agentliklərdən təklif gəlməyəcək. Xahiş edirik qəbul etmək üçün yuxarıda təqdim olunmuş " +
            "təkliflərdən birinə cavab verin, əgər cavab vermənisizsə. \n Bir necə müddətdən sonra sorğunuz dayandırılacaq!";

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