package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.Language;
import az.code.telegram_bot.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LanguageRepository extends JpaRepository<Question, Long> {
    @Query("select l from Language l where l.lang=:lang")
    Language getByLangName(String lang);
}
