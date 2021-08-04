package az.code.telegram_bot.services;

import az.code.telegram_bot.models.Language;
import az.code.telegram_bot.repositories.LanguageRepository;
import az.code.telegram_bot.services.Interfaces.LanguageService;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl implements LanguageService {
    final
    LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public Language getLanguage(long langId) {
        return languageRepository.getByLangId(langId);
    }
}
