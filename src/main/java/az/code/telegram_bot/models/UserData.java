package az.code.telegram_bot.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {
    Long langId;

    public UserData() {
        this.langId = -1L;
    }
}
