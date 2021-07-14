package az.code.telegram_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApplication.class, args);
    }

}
