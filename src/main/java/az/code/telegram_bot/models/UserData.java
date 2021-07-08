package az.code.telegram_bot.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class UserData {
    private Long langId;
    Map<String, String> answers = new HashMap<>();

    public UserData() {
        this.langId = 4L;
    }


}
