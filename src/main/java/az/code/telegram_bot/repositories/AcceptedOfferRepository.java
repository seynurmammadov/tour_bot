package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.AcceptedOffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AcceptedOfferRepository implements RedisRepository<AcceptedOffer> {

    public static final String HASH_KEY = "AcceptedOffer";

    private final RedisTemplate template;

    public AcceptedOfferRepository(RedisTemplate template) {
        this.template = template;
    }

    public AcceptedOffer save(Long userId, AcceptedOffer data) {
        template.opsForHash().put(HASH_KEY, userId, data);
        return data;
    }

    public AcceptedOffer findById(Long userId) {
        return (AcceptedOffer) template.opsForHash().get(HASH_KEY, userId);
    }


    public void delete(Long userId) {
        template.opsForHash().delete(HASH_KEY, userId);
    }
}