package az.code.telegram_bot.repositories;

import az.code.telegram_bot.models.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query("select l from Language l where l.lang=:lang")
    Language getByLangName(String lang);
    @Query("select l from Language l where l.id=:id")
    Language getByLangId(Long id);
}
