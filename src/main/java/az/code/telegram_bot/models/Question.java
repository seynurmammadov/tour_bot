package az.code.telegram_bot.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String state;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question", fetch = FetchType.EAGER)
    List<QuestionTranslate> questionTranslates;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question", fetch = FetchType.EAGER)
    Set<Action> actions;
}
