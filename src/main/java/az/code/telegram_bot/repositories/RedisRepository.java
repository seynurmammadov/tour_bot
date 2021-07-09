package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.UserData;

public interface RedisRepository<T> {
    T save(Long userId, T data);

    T findById(Long userId);

    void delete(Long userId);
}
