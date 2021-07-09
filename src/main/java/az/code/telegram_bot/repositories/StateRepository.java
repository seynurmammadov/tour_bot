package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.Question;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StateRepository implements RedisRepository<Question> {
    public static final String HASH_KEY = "State";

    private final RedisTemplate template;

    public StateRepository(RedisTemplate template) {
        this.template = template;
    }

    public Question save(Long userId, Question data) {
        template.opsForHash().put(HASH_KEY, userId, data);
        return data;
    }

    public Question findById(Long userId) {
        return (Question) template.opsForHash().get(HASH_KEY, userId);
    }


    public void delete(Long userId) {
        template.opsForHash().delete(HASH_KEY, userId);
    }
}
