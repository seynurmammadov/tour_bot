package az.code.telegram_bot.repositories;


public interface RedisRepository<T> {
    T save(Long userId, T data);

    T findById(Long userId);

    void delete(Long userId);
}
