package az.code.telegram_bot.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@RedisHash("UserData")
public class UserData implements Serializable {
    private Long langId;
    Map<String, String> answers = new HashMap<>();

    public UserData() {
        this.langId = 4L;
    }

    public void addAnswer(String answer, String question) {
        answers.put(question, answer);
    }

}
