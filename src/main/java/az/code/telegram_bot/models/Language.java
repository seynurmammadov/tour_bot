package az.code.telegram_bot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "languages")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String lang;
    @JsonIgnore
    @OneToMany(mappedBy = "language")
    List<QuestionTranslate> questionTranslates;
}
