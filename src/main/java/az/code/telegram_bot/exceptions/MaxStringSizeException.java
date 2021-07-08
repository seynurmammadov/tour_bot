package az.code.telegram_bot.exceptions;

public class MaxStringSizeException extends Exception implements MyCustomException{
    private final int maxLength;
    public MaxStringSizeException(int maxLength) {
        this.maxLength =maxLength;
    }
    //TODO get text with properties
    String ru = "Сообщение не может содержать больше %s символов!";
    String en = "The message cannot contain more %s characters!";
    String az = "Mesaj %s simvoldan çox ola bilməz!";
    @Override
    public String getLocalizedMessage(Long langId) {

        switch (langId.intValue()){
            case 1: return String.format(ru,maxLength);
            case 2: return String.format(az,maxLength);
            case 3: return String.format(en,maxLength);
            default:  return String.format(en,maxLength);
        }
    }
}
