package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.UserData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class UserDataRepository implements RedisRepository<UserData>{

    public static final String HASH_KEY = "UserData";

    private final RedisTemplate template;

    public UserDataRepository(RedisTemplate template) {
        this.template = template;
    }

    public UserData save(Long userId,UserData data) {
        template.opsForHash().put(HASH_KEY,userId, data);
        return data;
    }

    public UserData findById(Long userId) {
        return (UserData) template.opsForHash().get(HASH_KEY, userId);
    }


    public void delete(Long userId) {
        template.opsForHash().delete(HASH_KEY, userId);
    }
}
