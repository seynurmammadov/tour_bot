package az.code.telegram_bot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question_translates")
public class QuestionTranslate  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String context;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "question_id")
    Question question;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "lang_id")
    Language language;
}
