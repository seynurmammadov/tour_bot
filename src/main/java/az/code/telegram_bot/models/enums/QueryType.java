package az.code.telegram_bot.models.enums;

public enum QueryType {
    NEXT("NEXT");
    private final String val;

    public String getVal() {
        return val;
    }

    QueryType(String val) {
        this.val = val;
    }
}
